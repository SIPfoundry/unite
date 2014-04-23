package org.ezuce.common.rest;

public enum Folder {
    INBOX {
        public String toString() {
            return "inbox";
        }
    },
    SAVED {
        public String toString() {
            return "saved";
        }
    },
    DELETED {
        public String toString() {
            return "deleted";
        }
    },
    CONFERENCE {
        public String toString() {
            return "conference";
        }
    }
}
