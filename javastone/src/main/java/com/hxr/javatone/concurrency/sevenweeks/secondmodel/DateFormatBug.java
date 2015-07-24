/***
 * Excerpted from "Seven Concurrency Models in Seven Weeks",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
***/
package com.hxr.javatone.concurrency.sevenweeks.secondmodel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * {SimpleDateFormat内部有可变状态}
 * <br>
 *  
 * <p>
 * Create on : 2015年7月25日<br>
 * <p>
 * </p>
 * <br>
 * @author hanxirui<br>
 * @version javastone v1.0
 * <p>
 *<br>
 * <strong>Modify History:</strong><br>
 * user     modify_date    modify_content<br>
 * -------------------------------------------<br>
 * <br>
 */
class DateFormatBug {
  public static void main(final String[] args) throws Exception {
    final DateParser parser = new DateParser();
    final String dateString = "2012-01-01";
    final Date dateParsed = parser.parse(dateString);

    class ParsingThread extends Thread {
      @Override
    public void run() {
        try {
          while(true) {
            Date d = parser.parse(dateString);
            if (!d.equals(dateParsed)) {
              System.out.println("Expected: "+ dateParsed +", got: "+ d);
            }
          }
        } catch (Exception e) {
          System.out.println("Caught: "+ e);
        }
      }
    }

    Thread t1 = new ParsingThread();
    Thread t2 = new ParsingThread();
    t1.start();
    t2.start();
  }
}
class DateParser {
    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public Date parse(final String s) throws ParseException {
      return format.parse(s);
    }
  }
