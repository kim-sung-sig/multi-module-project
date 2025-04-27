package com.example.common.interfaces;

@FunctionalInterface
public interface ThrowingRunable {

    /**
     * Executes the run method, allowing for exceptions to be thrown.
     *
     * @throws Exception if an error occurs during execution
     */
    void run() throws Exception;

}
