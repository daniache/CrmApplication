package org.manmet.crmapplication.diagnostic;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Component
public class DiagnosticScanner {
    private final SimpMessagingTemplate messagingTemplate;

    // Inject SimpMessagingTemplate for WebSocket communication
    public DiagnosticScanner(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Method to scan classes and retrieve authorization information
    public List<ApiAuthorizationInfo> scanClasses() {
        List<ApiAuthorizationInfo> authInfoList = new ArrayList<>();

        // Scan for all classes in the package
        Reflections reflections = new Reflections("org.manmet.crmapplication", Scanners.MethodsAnnotated);
        Set<Method> methods = reflections.getMethodsAnnotatedWith(PreAuthorize.class);

        // Iterate through the methods found with @PreAuthorize annotation
        for (Method method : methods) {
            PreAuthorize preAuth = method.getAnnotation(PreAuthorize.class);
            Class<?> declaringClass = method.getDeclaringClass();  // Get the class where the method is declared

            String fullApiRoute = "";

            // Handle controller classes (marked with @RestController or @Controller)
            if (declaringClass.isAnnotationPresent(RestController.class) || declaringClass.isAnnotationPresent(Controller.class)) {
                // Get the class-level @RequestMapping (base URL)
                String basePath = "";
                if (declaringClass.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping classMapping = declaringClass.getAnnotation(RequestMapping.class);
                    if (classMapping.value().length > 0) {
                        basePath = classMapping.value()[0];  // Class-level base URL
                    }
                }
                // Get the method-level API route
                String apiRoute = getApiRoute(method);

                // Combine class-level and method-level paths to form the full API route
                fullApiRoute = basePath + apiRoute;
            } else {
                // For non-controller classes (e.g., services), use the method name as the "route"
                fullApiRoute = "/service/" + method.getName().toLowerCase();
            }

            // Create the authorization info
            ApiAuthorizationInfo authInfo = new ApiAuthorizationInfo(
                    declaringClass.getSimpleName(),  // Class name (controller or service)
                    method.getName(),                // Method name
                    fullApiRoute,                    // Full API route or method name
                    preAuth.value()                  // Required authorization (role/permission)
            );

            authInfoList.add(authInfo);
        }

        return authInfoList;  // Return the list of authorization info
    }

    // Extract API route based on method annotations (e.g., @RequestMapping, @GetMapping)
    private String getApiRoute(Method method) {
        // Example of extracting @RequestMapping or similar annotations for the API route
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping.value().length > 0) {
                return requestMapping.value()[0];  // Extract the API path
            }
        }

        // Check for @GetMapping annotation
        if (method.isAnnotationPresent(GetMapping.class)) {
            GetMapping getMapping = method.getAnnotation(GetMapping.class);
            if (getMapping.value().length > 0) {
                return getMapping.value()[0];  // Extract the API path from @GetMapping
            }
        }

        // Check for @PostMapping annotation
        if (method.isAnnotationPresent(PostMapping.class)) {
            PostMapping postMapping = method.getAnnotation(PostMapping.class);
            if (postMapping.value().length > 0) {
                return postMapping.value()[0];  // Extract the API path from @PostMapping
            }
        }

        // Check for @PutMapping annotation
        if (method.isAnnotationPresent(PutMapping.class)) {
            PutMapping putMapping = method.getAnnotation(PutMapping.class);
            if (putMapping.value().length > 0) {
                return putMapping.value()[0];  // Extract the API path from @PutMapping
            }
        }

        // Check for @DeleteMapping annotation
        if (method.isAnnotationPresent(DeleteMapping.class)) {
            DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
            if (deleteMapping.value().length > 0) {
                return deleteMapping.value()[0];  // Extract the API path from @DeleteMapping
            }
        }

        // Fallback to method name if no specific mapping annotation is found
        return "/" + method.getName().toLowerCase();
    }
}
