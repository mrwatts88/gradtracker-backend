package edu.uwm.capstone.controller;

import edu.uwm.capstone.service.exception.EntityNotFoundException;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Callable;

class RestControllerUtil {

    private RestControllerUtil() {
    } // removes sonarqube code smell

    static <T> T runCallable(Callable<T> callable, HttpServletResponse response, Logger log) throws IOException {
        try {
            return callable.call();
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, e.getMessage());
            return null;
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            return null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            return null;
        }
    }

    static void runRunnable(Runnable runnable, HttpServletResponse response, Logger log) throws IOException {
        try {
            runnable.run();
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, e.getMessage());
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
