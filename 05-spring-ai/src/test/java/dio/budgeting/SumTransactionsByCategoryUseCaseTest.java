package dio.budgeting.application;

import dio.budgeting.domain.Category;
import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SumTransactionsByCategoryUseCaseTest {

    @Test
    void should_sumAmounts_when_categoryHasTransactions() {
        var repository = mock(TransactionRepository.class);
        when(repository.findAllByCategory(Category.GROCERIES)).thenReturn(List.of(
                new Transaction("Arroz", 1500, Category.GROCERIES),
                new Transaction("Feijão", 800, Category.GROCERIES)));

        var result = new SumTransactionsByCategoryUseCase(repository).execute(Category.GROCERIES);

        assertEquals(2300, result.totalInCents());
        assertEquals("GROCERIES", result.category());
    }

    @Test
    void should_returnZero_when_categoryHasNoTransactions() {
        var repository = mock(TransactionRepository.class);
        when(repository.findAllByCategory(Category.AUTO)).thenReturn(List.of());

        var result = new SumTransactionsByCategoryUseCase(repository).execute(Category.AUTO);

        assertEquals(0, result.totalInCents());
    }
}