/*
 * HADARS - Cosylab Hadoop-based Accelerator Data Archiver and Retrieval System
 * Copyright (c) 2018 Cosylab d.d.
 *
 * mailto:info AT cosylab DOT com
 * Gerbiceva 64, 1000 Ljubljana, Slovenia
 *
 * This software is distributed under the terms found
 * in file LICENSE-CSL-2.0.txt that is included with this distribution.
 */

package com.cosylab.hadars.archiverappliance.retrieval.client;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Iterator;

import com.google.protobuf.ByteString;

import edu.stanford.slac.archiverappliance.PB.EPICSEvent.PayloadInfo;
import edu.stanford.slac.archiverappliance.PB.data.DBR2PBTypeMapping;
import org.epics.archiverappliance.ByteArray;
import org.epics.archiverappliance.Event;
import org.epics.archiverappliance.config.ArchDBRTypes;
import org.epics.archiverappliance.data.DBRTimeEvent;

/**
 * Generate a sequence of GeneratedMessage given an input stream...
 */
public class InputStreamBackedEventStream implements EventStream {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(InputStreamBackedEventStream.class.getName());
    private InputStream is;
    private byte[] isBuf = new byte[256 * 1024];
    private int currentReadPointer = 0;
    private int bytesRead = -1;
    private long filePos = 0;
    PayloadInfo info;
    InfoChangeHandler infoChangeHandler = null;
    // The size of the ByteBuffer here is related to the MAX_LINE sizes in LineByteStream...
    ByteBuffer buf = ByteBuffer.allocate(16 * 1024 * 1024);
    Event nextMsg = null;
    int currentLine = 0;

    public InputStreamBackedEventStream(InputStream is) throws IOException {
        this.is = is;
        readAndUnescapeLine(buf, true);
        info = PayloadInfo.parseFrom(ByteString.copyFrom(buf));
        if (this.infoChangeHandler != null) this.infoChangeHandler.handleInfoChange(info);
        readLineAndParseNextMessage();
    }

    @Override
    public PayloadInfo getPayLoadInfo() {
        return info;
    }

    @Override
    public Iterator<Event> iterator() {
        return new Iterator<Event>() {

            @Override
            public boolean hasNext() {
                return nextMsg != null;
            }

            @Override
            public Event next() {
                try {
                    Event ret = nextMsg;
                    readLineAndParseNextMessage();
                    return ret;
                } catch (IOException ex) {
                    throw new RuntimeIOException("Exception near line " + currentLine, ex);
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    /**
     * Calls to InputStream read are replaced with calls to this method instead
     */
    private void fetchData() throws IOException {
        currentReadPointer = 0;
        filePos += bytesRead;
        bytesRead = is.read(isBuf);
    }

    /**
     * Read a line into buf. Return true if we are exiting because of a newline; else return false.
     *
     * @param buf
     * @param unescaped, set to true for PayloadInfo, set to false for data
     * @return
     * @throws IOException
     */
    private boolean readAndUnescapeLine(ByteBuffer buf, boolean unescaped) throws IOException {
        buf.clear();
        byte next = -1;
        boolean hasNext = true;
        // This is equivalent to an is.read()
        if (currentReadPointer < bytesRead) {
            next = isBuf[currentReadPointer++];
        } else {
            fetchData();
            if (currentReadPointer < bytesRead) {
                next = isBuf[currentReadPointer++];
            } else {
                hasNext = false;
            }
        }
        // End of is.read()

        while (hasNext) {
            byte b = next;
            if (b == ESCAPE_CHAR && unescaped) {
                // This is equivalent to an is.read()
                if (currentReadPointer < bytesRead) {
                    next = isBuf[currentReadPointer++];
                } else {
                    fetchData();
                    if (currentReadPointer < bytesRead) {
                        next = isBuf[currentReadPointer++];
                    } else {
                        hasNext = false;
                    }
                }
                // End of is.read()
                if (!hasNext) {
                    throw new IOException("Escape character terminated early");
                }
                b = next;
                switch (b) {
                    case ESCAPE_ESCAPE_CHAR:
                        buf.put(ESCAPE_CHAR);
                        break;
                    case NEWLINE_ESCAPE_CHAR:
                        buf.put(NEWLINE_CHAR);
                        break;
                    case CARRIAGERETURN_ESCAPE_CHAR:
                        buf.put(CARRIAGERETURN_CHAR);
                        break;
                    default:
                        buf.put(b);
                        break;
                }
            } else if (b == NEWLINE_CHAR) {
                buf.flip();
                currentLine++;
                return true;
            } else {
                buf.put(b);
            }

            // This is equivalent to an is.read()
            if (currentReadPointer < bytesRead) {
                next = isBuf[currentReadPointer++];
            } else {
                fetchData();
                if (currentReadPointer < bytesRead) {
                    next = isBuf[currentReadPointer++];
                } else {
                    hasNext = false;
                }
            }
            // End of is.read()
        }

        buf.flip();
        currentLine++;
        return false;
    }

    private static final byte ESCAPE_CHAR = 0x1B;
    private static final byte ESCAPE_ESCAPE_CHAR = 0x01;
    private static final byte NEWLINE_CHAR = 0x0A;
    private static final byte NEWLINE_ESCAPE_CHAR = 0x02;
    private static final byte CARRIAGERETURN_CHAR = 0x0D;
    private static final byte CARRIAGERETURN_ESCAPE_CHAR = 0x03;

    private boolean loopInfoLine() throws IOException {
        int loopCount = 0;
        boolean haveNewline = readAndUnescapeLine(buf, false);
        while (loopCount++ < 1000) {
            if (!haveNewline && !buf.hasRemaining()) {
                // This is the end of the stream
                return false;
            } else if (haveNewline && !buf.hasRemaining()) {
                // We encountered an empty line. We expect a header next and data after that
                // year is changed.
                readAndUnescapeLine(buf, true);
                if (!buf.hasRemaining()) {
                    // We encountered an empty line and there was not enough info for a payload.
                    // We treat this as the end of the stream
                    return false;
                }
                info = PayloadInfo.parseFrom(ByteString.copyFrom(buf));
                if (this.infoChangeHandler != null) this.infoChangeHandler.handleInfoChange(info);
                haveNewline = readAndUnescapeLine(buf, false);
            } else {
                // Regardless of whether the line ended in a newline or not, we have data in buf
                return true;
            }
        }
        throw new IOException("We are unable to determine next event in " + loopCount + " loops");
    }

    private void readLineAndParseNextMessage() throws IOException {
        while (true) {
            boolean processNextMsg = loopInfoLine();
            if (!processNextMsg) {
                nextMsg = null;
                return;
            }

            try {
                ByteString byteString = ByteString.copyFrom(buf);
                byte[] bytes = byteString.toByteArray();
                ArchDBRTypes type = ArchDBRTypes.valueOf(info.getType());
                Constructor<? extends DBRTimeEvent> unmarshallingConstructor = DBR2PBTypeMapping.getPBClassFor(type)
                        .getUnmarshallingFromByteArrayConstructor();

                nextMsg = unmarshallingConstructor.newInstance((short) (info.getYear()), new ByteArray(bytes));
                return;
            } catch (Exception ex) {
                logger.error("Exception deserializing the PB.", ex);
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (is != null) {
            is.close();
            is = null;
        }
    }

    @Override
    public void onInfoChange(InfoChangeHandler handler) {
        this.infoChangeHandler = handler;
    }
}
