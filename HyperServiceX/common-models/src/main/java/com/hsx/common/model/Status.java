package com.hsx.common.model;

public class Status {

    public enum BORequestDetail {
        SENT(1), SUCCESS(2), FAIL(3), TIMEOUT(4);

        private int numVal;

        BORequestDetail(int numVal) {
            this.numVal = numVal;
        }

        public int getNumVal() {
            return numVal;
        }
    }

    public enum BORequest {
        SENT(1), PARTIAL_SUCCESS(2), COMPLETED(3), FAIL(4);

        private int numVal;

        BORequest(int numVal) {
            this.numVal = numVal;
        }

        public int getNumVal() {
            return numVal;
        }
    }

    public enum Resubmission {
        RESUBMISSION_CANDIDATE,
        NOT_RESUBMISSION_CANDIDATE,
        RESUBMITTED
    }
}
