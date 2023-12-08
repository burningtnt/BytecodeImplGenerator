package net.burningtnt.bcigenerator.api;

/**
 * Mark that this method is implemented by BytecodeImplGenerator.
 */
public class BytecodeImplError extends Error {
    public BytecodeImplError() {
        super("This method should be implemented by BytecodeImplGenerator.");
    }
}
