package com.hlsii.service;

import com.hazelcast.core.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Monitor Hazelcast cluster state
 */
@Service
public class HazelcastClusterStateMonitor implements IHazelcastClusterStateMonitor {
    private static Logger logger = Logger.getLogger(HazelcastClusterStateMonitor.class.getName());

    private ConcurrentSkipListSet<String> appliancesInCluster = new ConcurrentSkipListSet<>();
    private LifecycleEvent.LifecycleState clusterState = LifecycleEvent.LifecycleState.CLIENT_DISCONNECTED;

    @Override
    public void initialize(HazelcastInstance client) {
        if (client == null) {
            logger.error("The passed client parameter is null, cannot monitor Hazelcast Cluster State!");
            return;
        }
        try {
            Cluster cluster = client.getCluster();
            Set<Member> memberSet = cluster.getMembers();
            if (memberSet == null || memberSet.isEmpty()) {
                logger.warn("no member in cluster yet!");
            } else {
                for (Member member : memberSet) {
                    logger.info("Try to add member " + member.getAddress().toString() + " to my set.");
                    String aaString = buildAAString(member);
                    if (aaString != null) {
                        appliancesInCluster.add(aaString);
                    }
                }

                if (client.getLifecycleService().isRunning()) {
                    clusterState = LifecycleEvent.LifecycleState.CLIENT_CONNECTED;
                }
            }

            client.getLifecycleService().addLifecycleListener(event -> {
                logger.info("Cluster state is changed to " + event.getState());
                clusterState = event.getState();
            });

            logger.debug("Establishing a cluster membership listener to detect when appliances drop off the cluster");

            cluster.addMembershipListener(new MembershipListener() {
                public void memberAdded(MembershipEvent membersipEvent) {
                    Member member = membersipEvent.getMember();
                    logger.info("Member " + member.getAddress().toString() + " is added.");
                    String aaString = buildAAString(member);
                    if (aaString != null) {
                        appliancesInCluster.add(aaString);
                    }
                }

                public void memberRemoved(MembershipEvent membersipEvent) {
                    Member member = membersipEvent.getMember();
                    logger.info("Member " + member.getAddress().toString() + " is removed.");
                    String aaString = buildAAString(member);
                    if (aaString != null) {
                        appliancesInCluster.removeIf(x -> x.equalsIgnoreCase(aaString));
                    }
                }

                @Override
                public void memberAttributeChanged(MemberAttributeEvent membersipEvent) {
                    Member member = membersipEvent.getMember();
                    logger.info("Received membership attribute changed event for member " + member.getAddress().toString());
                }
            });
        } catch (Exception ex) {
            logger.error("cannot monitor Hazelcast Cluster State because exception.", ex);
        }
    }

    private String buildAAString(Member member) {
        try {
            InetAddress memberInetAddr = InetAddress.getByName(member.getAddress().getHost());
            return memberInetAddr.getHostAddress() + ":" + member.getAddress().getPort();
        } catch (Exception ex) {
            logger.error(MessageFormat.format("cannot build AA string from {0}:{1}",
                    member.getAddress().getHost(), member.getAddress().getPort()), ex);
        }
        return null;
    }

    @Override
    public Set<String> getAvailableAA() {
        if (clusterState == LifecycleEvent.LifecycleState.CLIENT_CONNECTED) {
            return appliancesInCluster;
        }

        return new HashSet<>();
    }
}
