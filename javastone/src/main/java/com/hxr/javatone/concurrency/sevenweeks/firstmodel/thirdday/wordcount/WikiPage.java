/***
 * Excerpted from "Seven Concurrency Models in Seven Weeks",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
***/
package com.hxr.javatone.concurrency.sevenweeks.firstmodel.thirdday.wordcount;

class WikiPage extends Page {
  private final String title;
  private final String text;

  public WikiPage(final String title, final String text) { this.title = title; this.text = text; }

  @Override
public String getTitle() { return title; }
  @Override
public String getText() { return text; }
}