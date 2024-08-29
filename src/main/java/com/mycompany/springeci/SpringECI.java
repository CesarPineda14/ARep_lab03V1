
package com.mycompany.springeci;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class SpringECI {

    public static void main(String[] args) throws ClassNotFoundException, MalformedURLException, IllegalAccessException, InvocationTargetException {
         Class c = Class.forName(args[0]);
         Map<String, Method> services = new HashMap();
         
         
         // Cargar componentes
         if (c.isAnnotationPresent(RestController.class)){
             Method[] methods = c.getDeclaredMethods();
             for (Method m: methods){
                 if(m.isAnnotationPresent(GetMapping.class)){
                     String Key = m.getAnnotation(GetMapping.class).value();
                     services.put(Key, m);
                 }
             }
         }
         
         //Codigo quemado para ejemplo
         
         URL serviceurl = new URL("http://localgost:8080/App/hello");
         String path = serviceurl.getPath();
         String servicename = path.substring(4);
         System.out.println("service name: "+ servicename);
         
         Method ms = services.get(servicename);
         System.out.println("service response: "+ ms.invoke(null));
         
         
         
         URL serviceurlpi = new URL("http://localgost:8080/App/pi");
         String pathpi = serviceurlpi.getPath();
         String servicenamepi = pathpi.substring(4);
         System.out.println("service name: "+ servicenamepi);
         
         Method mspi = services.get(servicenamepi);
         System.out.println("service response: "+ mspi.invoke(null));
         
         
         
         URL serviceurlrd = new URL("http://localgost:8080/App/random");
         String pathrd = serviceurlrd.getPath();
         String servicenamerd = pathrd.substring(4);
         System.out.println("service name: "+ servicenamepi);
         
         Method msrd = services.get(servicenamerd);
         System.out.println("service response: "+ msrd.invoke(null));
                 
    }
}
