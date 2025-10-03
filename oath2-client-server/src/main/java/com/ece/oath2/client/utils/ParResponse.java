package com.ece.oath2.client.utils;

public class ParResponse {
	private String request_uri;
    private int expires_in;

    // getters and setters
    public String getRequest_uri() { return request_uri; }
    public void setRequest_uri(String request_uri) { this.request_uri = request_uri; }
    public int getExpires_in() { return expires_in; }
    public void setExpires_in(int expires_in) { this.expires_in = expires_in; }
}
