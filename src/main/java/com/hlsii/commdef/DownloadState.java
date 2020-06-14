package com.hlsii.commdef;

/**
 * Download task state
 *
 */
public enum DownloadState {
    Created,       // Task is created
    Downloading,   // Download is in progress
    Canceled,      // Download is canceled from client
    Terminated,    // Download is terminated due to error on server,
    // or server detected client is closed.
    Finished       // Download is completed normally
}