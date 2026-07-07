package com.whereitgo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;

import com.whereitgo.exceptionHandling.DashboardExceptions;
import com.whereitgo.exceptionHandling.TransactionExceptions;
import com.whereitgo.model.Transaction;
import com.whereitgo.model.User;
import com.whereitgo.repository.TransactionRepo;
import com.whereitgo.utility.httpEntity.DashboardFilterDTO;
import com.whereitgo.utility.httpEntity.TransactionDTO;

@Service
public class DashboardService {

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private UserService userService;

    public List<TransactionDTO> getAllTransactions(DashboardFilterDTO filter) {

        try {

            User user = userService.getCurrentUser();

            List<Transaction> transactions = transactionRepo.findAll(
                    filterTransactions(user, filter));

            if (transactions.isEmpty()) {
                throw new DashboardExceptions.DashboardTransactionNotFoundException(
                        "No transactions found.");
            }

            List<TransactionDTO> response = new ArrayList<>();

            for (Transaction transaction : transactions) {

                TransactionDTO dto = new TransactionDTO();

                dto.setTransactionId(transaction.getId());
                dto.setTransactionType(transaction.getTransactionType());
                dto.setTransactionStatus(transaction.getTransactionStatus());
                dto.setAmount(transaction.getAmount());
                dto.setCurrency(transaction.getCurrency());
                dto.setBankName(transaction.getBankName());
                dto.setCounterpartyName(transaction.getCounterpartyName());
                dto.setTransactionTime(transaction.getTransactionTime());
                dto.setIsUserConfirmed(transaction.getIsUserConfirmed());

                response.add(dto);
            }

            return response;

        } catch (DashboardExceptions.DashboardTransactionNotFoundException ex) {

            // Let GlobalExceptionHandler handle business exception
            throw ex;

        } catch (Exception ex) {

            throw new DashboardExceptions.DashboardFetchException(
                    "Failed to fetch transactions. Please try again later.");
        }
    }

    private Specification<Transaction> filterTransactions(User user,
            DashboardFilterDTO filter) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Mandatory filter
            predicates.add(cb.equal(root.get("user"), user));

            if (filter.getTransactionType() != null) {
                predicates.add(cb.equal(
                        root.get("transactionType"),
                        filter.getTransactionType()));
            }

            if (filter.getTransactionStatus() != null) {
                predicates.add(cb.equal(
                        root.get("transactionStatus"),
                        filter.getTransactionStatus()));
            }

            if (filter.getBankName() != null &&
                    !filter.getBankName().isBlank()) {

                predicates.add(cb.equal(
                        cb.lower(root.get("bankName")),
                        filter.getBankName().toLowerCase()));
            }

            if (filter.getFromDate() != null &&
                    filter.getToDate() != null) {

                predicates.add(cb.between(
                        root.get("transactionTime"),
                        filter.getFromDate(),
                        filter.getToDate()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}