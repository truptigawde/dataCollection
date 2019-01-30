package org.cabi.pdc.models;

public class Reports {
    NationalReport NationalReportObject;
    MyMonthlyReport MyMonthlyReportObject;
    private String Message = null;

    // Getter Methods

    public NationalReport getNationalReport() {
        return NationalReportObject;
    }

    public MyMonthlyReport getMyMonthlyReport() {
        return MyMonthlyReportObject;
    }

    public String getMessage() {
        return Message;
    }

    // Setter Methods

    public void setNationalReport(NationalReport NationalReportObject) {
        this.NationalReportObject = NationalReportObject;
    }

    public void setMyMonthlyReport(MyMonthlyReport MyMonthlyReportObject) {
        this.MyMonthlyReportObject = MyMonthlyReportObject;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }
}