package com.whereitgo.utility;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WIGResponse<T> {

    private T response;

    private WIGResponseStatus status;

    public static <T> WIGResponse<T> success( T response, int statusCode, String statusMessage) {

        WIGResponse<T> wigResponse = new WIGResponse<>();

        wigResponse.setResponse(response);

        wigResponse.setStatus(
                new WIGResponseStatus(
                        statusCode,
                        statusMessage
                )
        );

        return wigResponse;
    }
}