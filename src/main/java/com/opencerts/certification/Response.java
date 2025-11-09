package com.opencerts.certification;

import java.util.Objects;

public class Response {

    private String text;

    public Response(String text) {
        this.text = text;
    }

    // Getters //


    // Modificadores //


    // Outros //

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return Objects.equals(text, response.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text);
    }
}
