package dio.budgeting.application.output;

import java.math.BigDecimal;

public record CategoryTotalOutput(String category, long totalInCents, BigDecimal totalInReais) {
}