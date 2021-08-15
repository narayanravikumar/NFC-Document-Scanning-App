package com.example.myapplication;

public class Details {

    private static String USN;
    private static String Id;
    private static String DocId;
    private static String Marks;
    private static String Name;
    // string variable for
        // storing employee name



        // an empty constructor is
        // required when using
        // Firebase Realtime Database.
        public Details() {

        }

        // created getter and setter methods
        // for all our variables.
        public String getStudentTag() {
            return Id;
        }
        public String getStudentDocid() {
            return DocId;
        }
        public String getStudentUsn() {
            return USN;
        }

        public String getStudentName() {
            return Name;
        }
        public String getStudentMarks() {
            return Marks;
        }

    public static void setId(String Idd) {
        Id = Idd;
    }
    public static void setDocId(String DocIdd) {
        DocId = DocIdd;
    }
    public static void setUSN(String USNd) {
        USN = USNd;
    }
    public static void setName(String Named) {
        Name = Named;
    }
    public static void setMarks(String Marksd) {
        Marks = Marksd;
    }
}

