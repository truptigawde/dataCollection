package org.cabi.pdc.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class Utility {

    public static int getDpFromPixels(Context context, int pixels) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, r.getDisplayMetrics());
    }

    public static String getUTCDateTime(Date inputDate) {
        if (inputDate == null) {
            return "";
        }
        Date time = inputDate;
        SimpleDateFormat outputFmt = new SimpleDateFormat("dd MMMM yyyy - HH:mm");
        outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        return outputFmt.format(time).toString();
    }

    public static String getDateFromDateTime(String dateTime) {
        try {
            String strDate = new SimpleDateFormat("dd MMMM yyyy").format(new SimpleDateFormat("dd MMMM yyyy").parse(dateTime));
            return strDate;
        } catch (ParseException pe) {
            pe.printStackTrace();
            return dateTime;
        }
    }

    public static String getGuid() {
        return genNumber() + genNumber() + '-' + genNumber() + '-' + genNumber() + '-' + genNumber() + '-' + genNumber() + genNumber() + genNumber();
    }

    private static String genNumber() {
        return Double.toHexString(Math.floor((1 + Math.random()) * 0x10000)).substring(4, 8);
    }

    public static String getStringFromBitmap(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public static Bitmap getBitmapFromString(String encodedImage) {
        String str = encodedImage;
        byte data[] = android.util.Base64.decode(str, android.util.Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        return bmp;
    }

//    dateFormatFx() {
//        var sessionDate = new Date()
//                , year = sessionDate.getFullYear()
//                , date = sessionDate.getDate()
//        , monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"]
//        , month = monthNames[sessionDate.getMonth()]
//                , hours = sessionDate.getHours()
//                , minute = sessionDate.getMinutes().toString()
//        if (minute.length == 1) {
//            minute = '0' + minute
//        }
//        var fullDate = date + " " + month + " " + year + " - " + hours + ":" + minute
//        return fullDate
//    }

//    sessionDateFormatFx(dateTime) {
//        var monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"]
//        , day = dateTime.split(' ')[0].toString()
//                , monthName = dateTime.split(' ')[1]
//                , year = dateTime.split(' ')[2]
//                , time = dateTime.split('-')[1].trim()
//                , convertedDate
//        if (monthNames.indexOf(monthName) != -1) {
//            var monthIndex = (monthNames.indexOf(monthName) + 1).toString()
//        }
//        if (monthIndex.length == 1) {
//            monthIndex = '0' + monthIndex
//        }
//        if (day.length == 1) {
//            day = '0' + day
//        }
//        convertedDate = year + "/" + monthIndex + "/" + day + " " + time
//        return convertedDate
//    }
}