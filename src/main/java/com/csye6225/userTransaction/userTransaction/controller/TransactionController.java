package com.csye6225.userTransaction.userTransaction.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.csye6225.userTransaction.userTransaction.Authorization.Authorization;
import com.csye6225.userTransaction.userTransaction.entity.Transaction;
import com.csye6225.userTransaction.userTransaction.entity.User;
import com.csye6225.userTransaction.userTransaction.repository.TransactionRepository;
import com.csye6225.userTransaction.userTransaction.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class TransactionController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private TransactionRepository transRepo;

	@Autowired
	private Authorization auth;

	private ObjectMapper mapper = new ObjectMapper();

	@GetMapping("/user/{id}/transaction")
	public List<Transaction> getAllTransaction(@PathVariable int id, HttpServletRequest request,
			HttpServletResponse response) {

		if (userRepo.existsById(id)) {
			User user = userRepo.findById(id).get();
			String[] values = auth.values(request);
			if (user.getEmail() == values[0] && BCrypt.checkpw(values[1], user.getPassword())) {
				return user.getTransactions();
			} else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return null;
			}
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}

	}

	@PostMapping("/user/{id}/transaction")
	public void addTransaction(@PathVariable int id, @Valid @RequestBody Transaction trans, BindingResult result
			,HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (userRepo.existsById(id)) {
			User user = userRepo.findById(id).get();
			if (result.hasErrors()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println(mapper.writeValueAsString("Missing feild in transaction!!!"));
			} else {

				String[] values = auth.values(request);
				if (user.getEmail().equals(values[0]) && BCrypt.checkpw(values[1], user.getPassword())) {
					userRepo.findById(id).get().addTransaction(trans);
					userRepo.save(user);
					response.setStatus(HttpServletResponse.SC_CREATED);
					response.getWriter().println(mapper.writeValueAsString("Transaction Saved"));
				} else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				}
				// } else {
				// response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				// response.getWriter().println(mapper.writeValueAsString("No such user!!!"));
				// }
			}
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println(mapper.writeValueAsString("No such user!!!"));
		}
	}

	@PutMapping("/user/{id}/transaction/{transId}")
	public void updateTransaction(@PathVariable int id, @PathVariable String transId,
			@Valid @RequestBody Transaction trans,BindingResult result ,HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException, IOException {

		if (userRepo.existsById(id)) {
			User user = userRepo.findById(id).get();
			if (result.hasErrors()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println(mapper.writeValueAsString("Missing feild in transaction!!!"));
			} else {
				String[] values = auth.values(request);
				if (user.getEmail().equals(values[0]) && BCrypt.checkpw(values[1], user.getPassword())) {
					List<Transaction> transactions = user.getTransactions();
					if (transactions.stream().anyMatch(t -> t.getId().equals(transId))) {
						Transaction t = transRepo.findById(transId).get();
						t.setDescription(trans.getDescription());
						t.setAmount(trans.getAmount());
						t.setDate(trans.getDate());
						t.setMerchant(trans.getMerchant());
						t.setCategory(trans.getCategory());
						transRepo.save(t);
					} else {
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					}
				} else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				}
			}
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println(mapper.writeValueAsString("No such user!!!"));
		}
	}

	@DeleteMapping("/user/{id}/transaction/{transId}")
	public void deleteTransaction(@PathVariable int id, @PathVariable String transId, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException, IOException {
		if (userRepo.existsById(id)) {
			User user = userRepo.findById(id).get();
			String[] values = auth.values(request);
			if (user.getEmail().equals(values[0]) && BCrypt.checkpw(values[1], user.getPassword())) {
				List<Transaction> transactions = user.getTransactions();
				if (transactions.stream().anyMatch(t -> t.getId().equals(transId))) {
					transRepo.deleteById(transId);
				} else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().println(mapper.writeValueAsString("No such transaction!!!"));
				}
			} else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println(mapper.writeValueAsString("No such user!!!"));
		}
	}

}
