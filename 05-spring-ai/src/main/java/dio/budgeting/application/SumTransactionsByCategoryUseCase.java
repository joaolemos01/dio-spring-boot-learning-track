package dio.budgeting.application;

import dio.budgeting.application.output.CategoryTotalOutput;
import dio.budgeting.domain.Category;
import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SumTransactionsByCategoryUseCase {
    private final TransactionRepository transactionRepository;

    public SumTransactionsByCategoryUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Tool(name = "sum-transactions-by-category",
            description = "Calcula o total gasto em uma categoria, somando todas as transações dela")
    public CategoryTotalOutput execute(@ToolParam(description = "Categoria de uma transação") Category category) {
        long totalInCents = transactionRepository.findAllByCategory(category).stream()
                .mapToLong(Transaction::getAmount)
                .sum();

        return new CategoryTotalOutput(category.name(), totalInCents, BigDecimal.valueOf(totalInCents, 2));
    }
}