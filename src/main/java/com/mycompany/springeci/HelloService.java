
package com.mycompany.springeci;

@RestController
public class HelloService {
    
    @GetMapping("/hello")
    public static String hello(){
        return "Hello World!";
    }
    
        @GetMapping("/pi")
    public static String pi(){
        return String.valueOf(Math.PI);
    }
    
        @GetMapping("/random")
    public static String random(){
        return String.valueOf(Math.random());
    }
}
