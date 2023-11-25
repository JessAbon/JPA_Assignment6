package com.yrgo.advice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class PerformanceTimingAdvice {

    public Object performTimingMeasurement(ProceedingJoinPoint method) throws Throwable {

        //before
        long startTime =System.nanoTime();

        try {
            //proceed to target
            Object value= method.proceed();
            return value;

        }finally {
            //after
            long endTime= System.nanoTime();
            long timeTaken = endTime - startTime;
            System.out.println("\n***********************************************\nThe method " +
                    method.getSignature().getName() + "from the class:\n" + method.getTarget().getClass().getName() + " took " + timeTaken
                    /1000000 + " milliseconds. \n***********************************************\n ");
        }
    }

}
