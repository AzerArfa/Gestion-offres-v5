package com.ecom.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

	public static final List<String> openApiEndpoints = Arrays.asList(
		    "/auth/login",
		    "/auth/signup",
		    "/offer/visitor/appeloffres",
		    "/offer/visitor/categories",
	        "/offer/visitor/categories/{categorieId}/appeloffres" 
		);


    public Predicate<ServerHttpRequest> isSecured =
        request -> openApiEndpoints.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));

}
