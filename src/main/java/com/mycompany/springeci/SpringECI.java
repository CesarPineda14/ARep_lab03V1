package com.mycompany.springeci;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class SpringECI {

    private static Map<String, Method> services = new HashMap<>();

    static {
        try {
            // Escanea y registra los métodos anotados con @GetMapping
            registerController(HelloService.class);

            // Imprime las rutas registradas
            System.out.println("Servicios registrados:");
            for (String key : services.keySet()) {
                System.out.println("Servicio: " + key);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void registerController(Class<?> controllerClass) throws ClassNotFoundException {
        if (controllerClass.isAnnotationPresent(RestController.class)) {
            Method[] methods = controllerClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(GetMapping.class)) {
                    String key = method.getAnnotation(GetMapping.class).value();
                    services.put(key, method);
                }
            }
        }
    }

    public static String handleRequest(String path) {
        try {
            String[] pathParts = path.split("\\?");
            String serviceName = pathParts[0];
            String queryString = pathParts.length > 1 ? pathParts[1] : "";
            Method method = services.get(serviceName);

            System.out.println("metodo encontrado: " + method);
            System.out.println("Entra en el metodo eci: " + path);

            if (method != null) {

                Map<String, String> queryParams = parseQueryString(queryString);

                Object[] args = getMethodArguments(method, queryParams);

                HelloService serviceInstance = new HelloService();
                Object result = method.invoke(serviceInstance, args);

                System.out.println("Resultado del método: " + result);
                return (String) result;
            } else {
                System.out.println("Resultado del método: null");
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Map<String, String> parseQueryString(String queryString) {
        Map<String, String> queryParams = new HashMap<>();
        if (!queryString.isEmpty()) {
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return queryParams;
    }

    private static Object[] getMethodArguments(Method method, Map<String, String> queryParams) {

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];

        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof RequestParam) {
                    String paramName = ((RequestParam) annotation).value();
                    String paramValue = queryParams.getOrDefault(paramName, ((RequestParam) annotation).defaultValue());
                    args[i] = convertToType(paramValue, parameterTypes[i]);
                }
            }
        }

        return args;
    }

    private static Object convertToType(String value, Class<?> type) {
        if (type == String.class) {
            return value;
        } else if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        } else if (type == long.class || type == Long.class) {
            return Long.parseLong(value);
        }
        return null;
    }
}
