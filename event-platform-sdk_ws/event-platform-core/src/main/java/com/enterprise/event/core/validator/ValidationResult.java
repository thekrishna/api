package com.enterprise.event.core.validator;

import java.io.Serializable;

public record ValidationResult(boolean valid, String message) implements Serializable {
}
