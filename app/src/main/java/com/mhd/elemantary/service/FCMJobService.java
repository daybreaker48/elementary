package com.mhd.elemantary.service;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.mhd.elemantary.util.MHDLog;

/**
 * Created by MH D on 2017-12-24.
 */

public class FCMJobService extends JobService {
    private final String TAG = getClass().getName();

    @Override
    public boolean onStartJob(JobParameters job) {
        MHDLog.d(TAG, "Performing long running task in scheduled job");
        // TODO(developer): add long running task here.

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
