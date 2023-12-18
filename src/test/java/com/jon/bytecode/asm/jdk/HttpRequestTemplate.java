package com.jon.bytecode.asm.jdk;

public interface HttpRequestTemplate {

    HttpResponse doGet(HttpRequest httpRequest);

    HttpResponse doPost(HttpRequest httpRequest);

}