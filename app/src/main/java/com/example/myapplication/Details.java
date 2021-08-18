//Sets data to Firebase realtime database. It is the standard way to set a data to firebase realtime database
package com.example.myapplication;

//Class to set and retrieve details from Firebase
public class Details {

    String USN;
    String Id;
    String DocId;
    String Marks;
    String Name;
    // string variable for
    // storing employee name

    public Details(String Id,String DocId, String USN, String Name,String Marks){
        this.USN=USN;
        this.Id=Id;
        this.DocId=DocId;
        this.Marks=Marks;
        this.Name=Name;

    }

    public Details() {
    }

    public String getUSN() {
        return USN;
    }

    public void setUSN(String USN) {
        this.USN = USN;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDocId() {
        return DocId;
    }

    public void setDocId(String docId) {
        DocId = docId;
    }

    public String getMarks() {
        return Marks;
    }

    public void setMarks(String marks) {
        Marks = marks;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}


