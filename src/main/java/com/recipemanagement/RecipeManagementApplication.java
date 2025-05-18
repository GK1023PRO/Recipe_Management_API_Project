package com.recipemanagement;

import com.recipemanagement.dto.LoginRequest;
import com.recipemanagement.dto.RecipeRequest;
import com.recipemanagement.dto.RegisterRequest;
import com.recipemanagement.model.Recipe;
import com.recipemanagement.model.Role;
import com.recipemanagement.model.User;
import com.recipemanagement.repository.RecipeRepository;
import com.recipemanagement.repository.UserRepository;
import com.recipemanagement.service.AuthService;
import com.recipemanagement.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Bootstraps Spring application and implements command-line interface.
 */
@SpringBootApplication
public class RecipeManagementApplication implements CommandLineRunner {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_RED = "\u001B[31m";

    private final AuthService authService;
    private final RecipeService recipeService;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    private final List<String> transactionHistory = new ArrayList<>();

    private String currentToken = null;
    private String currentUsername = null;
    private boolean isAdmin = false;
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public RecipeManagementApplication(AuthService authService,
                                       RecipeService recipeService,
                                       RecipeRepository recipeRepository,
                                       UserRepository userRepository, MongoTemplate mongoTemplate) {
        this.authService = authService;
        this.recipeService = recipeService;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.mongoTemplate=mongoTemplate;
    }






    /**
     * Command line interface entry point
     * @param args Command line arguments
     */
    @Override
    public void run(String... args) {
        clearScreen();
        printHeader();

        boolean exit = false;
        while (!exit) {
            if (currentUsername == null) {
                showLoginMenu();
            } else if (isAdmin) {
                showAdminMenu();
            } else {
                showUserMenu();
            }

            System.out.print(ANSI_YELLOW + "Enter your choice: " + ANSI_RESET);
            String choice = scanner.nextLine();

            if (currentUsername == null) {
                exit = handleLoginMenu(choice);
            } else if (isAdmin) {
                exit = handleAdminMenu(choice);
            } else {
                exit = handleUserMenu(choice);
            }
        }

        System.out.println(ANSI_GREEN + "Thank you for using Recipe Management System. Goodbye!" + ANSI_RESET);
        scanner.close();
    }

    private void printHeader() {
        System.out.println(ANSI_BLUE + " ╔═══════════════════════════════════════════╗");
        System.out.println("  ║           RECIPE MANAGEMENT SYSTEM        ║");
        System.out.println("  ╚═══════════════════════════════════════════╝" + ANSI_RESET);
    }

    private void showLoginMenu() {
        System.out.println(ANSI_GREEN + "\nPlease select an option:");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Exit" + ANSI_RESET);
    }

    private void showUserMenu() {
        System.out.println(ANSI_GREEN + "\nLogged in as: " + ANSI_BLUE + currentUsername + ANSI_GREEN + " (CLIENT)");
        System.out.println("\nPlease select an option:");
        System.out.println("1. View All Recipes");
        System.out.println("2. Search Recipe by ID");
        System.out.println("3. Filter Recipes by Category and Cooking Time");
        System.out.println("4. Select Recipe from List");
        System.out.println("5. View My Transaction History");
        System.out.println("6. Logout");
        System.out.println("0. Exit" + ANSI_RESET);
    }

    private void showAdminMenu() {
        System.out.println(ANSI_GREEN + "\nLogged in as: " + ANSI_BLUE + currentUsername + ANSI_GREEN + " (ADMIN)");
        System.out.println("\nPlease select an option:");
        System.out.println("1. View All Recipes");
        System.out.println("2. Search Recipe by ID");
        System.out.println("3. Filter Recipes by Category and Cooking Time");
        System.out.println("4. Add New Recipe");
        System.out.println("5. Update Recipe");
        System.out.println("6. Delete Recipe");
        System.out.println("7. View All Users");
        System.out.println("8. Select Recipe from List");
        System.out.println("9. View My Transaction History");
        System.out.println("10. View All Transaction History");
        System.out.println("11. Logout");
        System.out.println("0. Exit" + ANSI_RESET);
    }

    private boolean confirmExit() {
        System.out.println(ANSI_YELLOW + "Are you sure you want to exit? (y/n): " + ANSI_RESET);
        String answer = scanner.nextLine().trim().toLowerCase();
        return answer.equals("y") || answer.equals("yes");
    }

    private boolean handleLoginMenu(String choice) {
        switch (choice) {
            case "1":
                login();
                return false;
            case "2":
                register();
                return false;
            case "0":
                System.exit(0);
                return true;
            default:
                System.out.println(ANSI_RED + "Invalid choice. Please try again." + ANSI_RESET);
                return false;
        }
    }

    private boolean handleUserMenu(String choice) {
        switch (choice) {
            case "1":
                viewAllRecipes();
                return false;
            case "2":
                searchRecipeById();
                return false;
            case "3":
                filterRecipes();
                return false;

            case "4":
                selectRecipeFromList();
                return false;
            case "5":
                viewMyTransactionHistory();
                return false;
            case "6":
                logout();
                return false;
            case "0":
                System.exit(0);
                return true;
            default:
                System.out.println(ANSI_RED + "Invalid choice. Please try again." + ANSI_RESET);
                return false;
        }
    }

    private boolean handleAdminMenu(String choice) {
        switch (choice) {
            case "1":
                viewAllRecipes();
                return false;
            case "2":
                searchRecipeById();
                return false;
            case "3":
                filterRecipes();
                return false;
            case "4":
                addNewRecipe();
                return false;
            case "5":
                updateRecipe();
                return false;
            case "6":
                deleteRecipe();
                return false;
            case "7":
                viewAllUsers();
                return false;
            case "8":
                selectRecipeFromList();
                return false;
            case "9":
                viewMyTransactionHistory();
                return false;
            case "10":
                viewAllTransactionHistory();
                return false;
            case "11":
                logout();
                return false;
            case "0":
                System.exit(0);
                return true;
            default:
                System.out.println(ANSI_RED + "Invalid choice. Please try again." + ANSI_RESET);
                return false;
        }
    }

    private void login() {
        System.out.println(ANSI_BLUE + "\n--- Login ---" + ANSI_RESET);
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        try {
            String token = authService.login(loginRequest);
            currentToken = token;
            currentUsername = username;

            userRepository.findByUsername(username).ifPresent(user -> {
                isAdmin = user.getRoles().contains(Role.ADMIN);
                String welcomeMsg = String.format(ANSI_GREEN + "\nWelcome %s! (%s role)" + ANSI_RESET,
                        user.getUsername(),
                        isAdmin ? "ADMIN" : "CLIENT");
                System.out.println(welcomeMsg);
                System.out.println("════════════════════════════════════════════════════════════════");
                System.out.println("       LOGIN SUCCESSFUL                                         ");
                System.out.println("════════════════════════════════════════════════════════════════");
                System.out.println("Logged in as:" + String.format("%-19s", user.getUsername()));
                System.out.println("Role:" + String.format("%-19s", user.getRoles()));
                System.out.println("════════════════════════════════════════════════════════════════");
            });


        } catch (Exception e) {
            System.out.println(ANSI_RED + "Login failed: " + e.getMessage() + ANSI_RESET);

        }
    }

    private void register() {
        System.out.println(ANSI_BLUE + "\n--- Register ---" + ANSI_RESET);
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.println("Select role:");
        System.out.println("1. CLIENT");
        System.out.println("2. ADMIN");
        System.out.print("Enter choice (1-2): ");
        String roleChoice = scanner.nextLine();

        Role role = roleChoice.equals("2") ? Role.ADMIN : Role.CLIENT;

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(username);
        registerRequest.setEmail(email);
        registerRequest.setPassword(password);
        registerRequest.setRole(role);

        try {
            authService.register(registerRequest);
            User newUser = userRepository.findByUsername(username).orElseThrow();
            System.out.println(ANSI_GREEN + "Registration successful! You can now login." + ANSI_RESET);
            System.out.println("═════════════════════════════════════════════════════════════════════════════");
            System.out.println("          USER DETAILS                                                       ");
            System.out.println("═════════════════════════════════════════════════════════════════════════════");
            System.out.println("ID:" + String.format("%-19s", newUser.getId()));
            System.out.println("Username:" + String.format("%-19s", newUser.getUsername()));
            System.out.println("Email:" + String.format("%-19s", truncate(newUser.getEmail(), 19)));
            System.out.println("Role:" + String.format("%-19s", newUser.getRoles()));
            System.out.println("═════════════════════════════════════════════════════════════════════════════" + ANSI_RESET);


        } catch (Exception e) {
            System.out.println(ANSI_RED + "Registration failed: " + e.getMessage() + ANSI_RESET);

        }
    }

    private void logout() {
        currentToken = null;
        currentUsername = null;
        isAdmin = false;
        System.out.println(ANSI_GREEN + "Logged out successfully!" + ANSI_RESET);

    }

    private void viewAllRecipes() {
        System.out.println(ANSI_BLUE + "\n--- All Recipes ---" + ANSI_RESET);
        System.out.print("Page number (starts at 0): ");
        int page = Integer.parseInt(scanner.nextLine());
        System.out.print("Page size: ");
        int size = Integer.parseInt(scanner.nextLine());
        System.out.println("Sort by (title, cookingTime, category): ");
        String sortBy = scanner.nextLine();
        if (sortBy.isEmpty()) sortBy = "title";

        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
            Page<Recipe> recipes = recipeService.getAllRecipes(pageable);

            if (recipes.isEmpty()) {
                System.out.println(ANSI_YELLOW + "No recipes found!" + ANSI_RESET);
            } else {
                System.out.println(ANSI_BLUE + "📖 Page " + (page + 1) + " of " + recipes.getTotalPages()
                        + " (Total recipes: " + recipes.getTotalElements() + ")" + ANSI_RESET);
                System.out.println("═════════════════════════════════════════════════════");

                int counter = 1;
                for (Recipe recipe : recipes.getContent()) {
                    System.out.println(ANSI_YELLOW + "🍴 Recipe #" + (counter + (page * size)) + ANSI_RESET);
                    System.out.println("────────────────────────────────────────────");
                    System.out.println(String.format("%-8s: %s", "ID", recipe.getId()));
                    System.out.println(String.format("%-8s: %s", "Title", truncate(recipe.getTitle(), 35)));
                    System.out.println(String.format("%-8s: %s", "Category", recipe.getCategory()));
                    System.out.println(String.format("%-8s: %s", "Cook Time", recipe.getCookingTime() + " minutes"));
                    System.out.println(String.format("%-8s: %d ingredients", "Items", recipe.getIngredients().size()));
                    System.out.println("═════════════════════════════════════════════════════");
                    counter++;
                }
                // New recipe selection prompt
                System.out.print(ANSI_YELLOW + "\nEnter recipe number to view details (or press Enter to continue): " + ANSI_RESET);
                String input = scanner.nextLine();
                if (!input.isEmpty()) {
                    try {
                        int selectedNumber = Integer.parseInt(input);
                        int index = selectedNumber - 1 - (page * size);
                        if (index >= 0 && index < recipes.getContent().size()) {
                            Recipe selectedRecipe = recipes.getContent().get(index);
                            displayRecipeDetails(selectedRecipe);
                        } else {
                            System.out.println(ANSI_RED + "Invalid recipe number." + ANSI_RESET);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(ANSI_RED + "Invalid input format." + ANSI_RESET);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
        }



    }

    private void searchRecipeById() {
        System.out.println(ANSI_BLUE + "\n--- Search Recipe by ID ---" + ANSI_RESET);
        System.out.print("Enter Recipe ID: ");
        String id = scanner.nextLine();

        try {
            Recipe recipe = recipeService.getRecipeById(id);
            displayRecipeDetails(recipe);
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
        }


    }

    private void filterRecipes() {
        System.out.println(ANSI_BLUE + "\n--- Filter Recipes ---" + ANSI_RESET);
        System.out.print("Category (leave empty to skip): ");
        String category = scanner.nextLine();

        System.out.print("Maximum cooking time in minutes (leave empty to skip): ");
        String maxTimeStr = scanner.nextLine();
        Integer maxCookingTime = maxTimeStr.isEmpty() ? null : Integer.parseInt(maxTimeStr);

        try {
            if (category.isEmpty()) {
                category = null;
            }

            List<Recipe> recipes = recipeService.filterRecipes(category, maxCookingTime);

            if (recipes.isEmpty()) {
                System.out.println(ANSI_YELLOW + "No recipes found with the given criteria!" + ANSI_RESET);
            } else {
                System.out.println(ANSI_GREEN + "\nFound " + recipes.size() + " recipes:" + ANSI_RESET);

                for (Recipe recipe : recipes) {
                    System.out.println(ANSI_BLUE + "\n───────────────────────────────────────────────" + ANSI_RESET);
                    System.out.println(ANSI_GREEN + "  Title: " + ANSI_RESET + recipe.getTitle());
                    System.out.println(ANSI_YELLOW + "  ID: " + ANSI_RESET + recipe.getId());
                    System.out.println(ANSI_YELLOW + "  Category: " + ANSI_RESET + recipe.getCategory());
                    System.out.println(ANSI_YELLOW + "  Cooking Time: " + ANSI_RESET + recipe.getCookingTime() + " minutes");
                }
                System.out.println(ANSI_BLUE + "───────────────────────────────────────────────" + ANSI_RESET);
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
        }
    }

    private void addNewRecipe() {
        System.out.println(ANSI_BLUE + "\n--- Add New Recipe ---" + ANSI_RESET);

        try {
            RecipeRequest request = new RecipeRequest();

            System.out.print("Title: ");
            request.setTitle(scanner.nextLine());

            System.out.print("Category: ");
            request.setCategory(scanner.nextLine());

            System.out.print("Cooking Time (minutes): ");
            request.setCookingTime(Integer.parseInt(scanner.nextLine()));

            System.out.println("Enter ingredients (one per line, empty line to finish):");
            List<String> ingredients = new ArrayList<>();
            while (true) {
                String ingredient = scanner.nextLine();
                if (ingredient.isEmpty()) break;
                ingredients.add(ingredient);
            }
            request.setIngredients(ingredients);

            System.out.println("Enter instructions:");
            request.setInstructions(scanner.nextLine());

            Recipe recipe = recipeService.createRecipe(request);

            System.out.println(ANSI_GREEN + "\nRecipe created successfully!");
            System.out.println("════════════════════════════════════════════");
            System.out.println("            NEW RECIPE DETAILS             ");
            System.out.println("════════════════════════════════════════════");
            System.out.println(" ID                 " + String.format("%-21s", recipe.getId()));
            System.out.println(" Title              " + String.format("%-21s", truncate(recipe.getTitle(), 21)));
            System.out.println(" Category           " + String.format("%-21s", truncate(recipe.getCategory(), 21)));
            System.out.println(" Cooking Time       " + String.format("%-21s", recipe.getCookingTime() + " mins"));
            System.out.println(" Ingredients        " + String.format("%-21s", recipe.getIngredients().size() + " items"));
            System.out.println("════════════════════════════════════════════");
            System.out.println(ANSI_YELLOW + "\nPreview of first ingredient: " + ANSI_RESET +
                    (recipe.getIngredients().isEmpty() ? "None" : recipe.getIngredients().get(0)));
            String transactionDescription = "Created new recipe: " + recipe.getTitle();
            logTransaction(currentUsername, "RECIPE_CREATION", recipe.getId(), transactionDescription);

        } catch (Exception e) {
            System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);

        }
    }

    private void updateRecipe() {
        System.out.println(ANSI_BLUE + "\n--- Update Recipe ---" + ANSI_RESET);
        System.out.print("Enter Recipe ID to update: ");
        String id = scanner.nextLine();

        try {
            Recipe existingRecipe = recipeService.getRecipeById(id);
            System.out.println("Current recipe details:");
            displayRecipeDetails(existingRecipe);

            System.out.println("\nEnter new details (press Enter to keep current value):");

            RecipeRequest request = new RecipeRequest();

            System.out.print("Title [" + existingRecipe.getTitle() + "]: ");
            String title = scanner.nextLine();
            request.setTitle(title.isEmpty() ? existingRecipe.getTitle() : title);

            System.out.print("Category [" + existingRecipe.getCategory() + "]: ");
            String category = scanner.nextLine();
            request.setCategory(category.isEmpty() ? existingRecipe.getCategory() : category);

            System.out.print("Cooking Time [" + existingRecipe.getCookingTime() + "]: ");
            String cookingTime = scanner.nextLine();
            request.setCookingTime(cookingTime.isEmpty() ?
                    existingRecipe.getCookingTime() :
                    Integer.parseInt(cookingTime));

            System.out.println("Current ingredients:");
            for (String ingredient : existingRecipe.getIngredients()) {
                System.out.println("- " + ingredient);
            }

            System.out.println("Enter new ingredients (one per line, empty line to finish):");
            List<String> ingredients = new ArrayList<>();
            while (true) {
                String ingredient = scanner.nextLine();
                if (ingredient.isEmpty()) break;
                ingredients.add(ingredient);
            }
            request.setIngredients(ingredients.isEmpty() ?
                    existingRecipe.getIngredients() :
                    ingredients);

            System.out.println("Current instructions: " + existingRecipe.getInstructions());
            System.out.println("Enter new instructions (or press Enter to keep current):");
            String instructions = scanner.nextLine();
            request.setInstructions(instructions.isEmpty() ?
                    existingRecipe.getInstructions() :
                    instructions);

            Recipe updatedRecipe = recipeService.updateRecipe(id, request);
            System.out.println(ANSI_GREEN + "Recipe updated successfully!" + ANSI_RESET);
            String transactionDescription = "Updated recipe: " + updatedRecipe.getTitle();
            logTransaction(currentUsername, "RECIPE_UPDATE", id, transactionDescription);
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
        }


    }

    private void deleteRecipe() {
        System.out.println(ANSI_BLUE + "\n--- Delete Recipe ---" + ANSI_RESET);
        System.out.print("Enter Recipe ID to delete: ");
        String id = scanner.nextLine();

        try {
            Recipe recipe = recipeService.getRecipeById(id);
            System.out.println("Are you sure you want to delete this recipe?");
            displayRecipeDetails(recipe);

            System.out.print("Type 'YES' to confirm deletion: ");
            String confirmation = scanner.nextLine();

            if (confirmation.equals("YES")) {
                recipeService.deleteRecipe(id);
                System.out.println(ANSI_GREEN + "Recipe deleted successfully!" + ANSI_RESET);
                String transactionDescription = "Deleted recipe: " + recipe.getTitle();
                logTransaction(currentUsername, "RECIPE_DELETION", id, transactionDescription);
            } else {
                System.out.println(ANSI_YELLOW + "Deletion cancelled." + ANSI_RESET);
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
        }


    }

    private void viewAllUsers() {
        System.out.println(ANSI_BLUE + "\n--- All Users ---" + ANSI_RESET);

        try {
            List<User> users = userRepository.findAll();

            if (users.isEmpty()) {
                System.out.println(ANSI_YELLOW + "No users found!" + ANSI_RESET);
            } else {
                System.out.println(ANSI_BLUE + "Total users: " + users.size() + ANSI_RESET);
                System.out.println("════════════════════════════════════════════");

                int counter = 1;
                for (User user : users) {
                    System.out.println(ANSI_YELLOW + "User #" + counter + ANSI_RESET);
                    System.out.println("────────────────────────────────────────────");
                    System.out.println(String.format("%-12s: %s", "ID", user.getId()));
                    System.out.println(String.format("%-12s: %s", "Username", truncate(user.getUsername(), 25)));
                    System.out.println(String.format("%-12s: %s", "Email", truncate(user.getEmail(), 30)));
                    System.out.println(String.format("%-12s: %s", "Roles", user.getRoles()));
                    System.out.println("════════════════════════════════════════════");
                    counter++;
                }
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
        }


    }

    private void displayRecipeDetails(Recipe recipe) {
        System.out.println(ANSI_BLUE + "\n--- Recipe Details ---" + ANSI_RESET);
        System.out.println("ID: " + recipe.getId());
        System.out.println("Title: " + recipe.getTitle());
        System.out.println("Category: " + recipe.getCategory());
        System.out.println("Cooking Time: " + recipe.getCookingTime() + " minutes");

        System.out.println("\nIngredients:");
        for (String ingredient : recipe.getIngredients()) {
            System.out.println("- " + ingredient);
        }

        System.out.println("\nInstructions:");
        System.out.println(recipe.getInstructions());
    }



    // Utility methods
    private void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
    private String truncate(String text, int length) {
        return text.length() > length ? text.substring(0, length - 3) + "..." : text;
    }


    /**
     * New method to select a recipe from a paginated list
     */
    private void selectRecipeFromList() {
        System.out.println(ANSI_BLUE + "\n--- Select Recipe from List ---" + ANSI_RESET);

        try {
            int page = 0;
            int size = 5;
            String sortBy = "title";
            boolean exit = false;

            while (!exit) {
                Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
                Page<Recipe> recipesPage = recipeService.getAllRecipes(pageable);

                if (recipesPage.isEmpty()) {
                    System.out.println(ANSI_YELLOW + "No recipes found!" + ANSI_RESET);
                    return;
                }

                List<Recipe> recipes = recipesPage.getContent();

                System.out.println(ANSI_BLUE + "📖 Page " + (page + 1) + " of " + recipesPage.getTotalPages()
                        + " (Total recipes: " + recipesPage.getTotalElements() + ")" + ANSI_RESET);
                System.out.println("═════════════════════════════════════════════════════");

                for (int i = 0; i < recipes.size(); i++) {
                    Recipe recipe = recipes.get(i);
                    System.out.println(ANSI_YELLOW + "[" + (i + 1) + "] " + recipe.getTitle() + ANSI_RESET);
                    System.out.println("    Category: " + recipe.getCategory());
                    System.out.println("    Cooking Time: " + recipe.getCookingTime() + " minutes");
                    System.out.println("────────────────────────────────────────────");
                }

                System.out.println(ANSI_GREEN + "\nOptions:" + ANSI_RESET);
                System.out.println("[N] Next page");
                System.out.println("[P] Previous page");
                System.out.println("[X] Exit to menu");
                System.out.println("[1-" + recipes.size() + "] Select a recipe");

                System.out.print(ANSI_YELLOW + "Enter your choice: " + ANSI_RESET);
                String choice = scanner.nextLine().trim();

                if ("X".equalsIgnoreCase(choice)) {
                    exit = true;
                } else if ("N".equalsIgnoreCase(choice)) {
                    if (page < recipesPage.getTotalPages() - 1) {
                        page++;
                    } else {
                        System.out.println(ANSI_RED + "Already on last page!" + ANSI_RESET);
                    }
                } else if ("P".equalsIgnoreCase(choice)) {
                    if (page > 0) {
                        page--;
                    } else {
                        System.out.println(ANSI_RED + "Already on first page!" + ANSI_RESET);
                    }
                } else {
                    try {
                        int recipeIndex = Integer.parseInt(choice) - 1;
                        if (recipeIndex >= 0 && recipeIndex < recipes.size()) {
                            Recipe selectedRecipe = recipes.get(recipeIndex);
                            displayRecipeDetails(selectedRecipe);
                            confirmRecipeSelection(selectedRecipe);
                        } else {
                            System.out.println(ANSI_RED + "Invalid selection. Please try again." + ANSI_RESET);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(ANSI_RED + "Invalid input. Please try again." + ANSI_RESET);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
        }
    }

    /**
     * Helper method to confirm recipe selection and log the transaction
     */
    private void confirmRecipeSelection(Recipe recipe) {
        System.out.print(ANSI_YELLOW + "\nWould you like to select this recipe? (Y/N): " + ANSI_RESET);
        String choice = scanner.nextLine().trim().toUpperCase();

        if (choice.equals("Y")) {
            logTransaction(currentUsername, "RECIPE_SELECTION", recipe.getId(), "Selected recipe: " + recipe.getTitle());
            System.out.println(ANSI_GREEN + "\nRecipe selected successfully!" + ANSI_RESET);

            System.out.print(ANSI_YELLOW + "View detailed instructions? (Y/N): " + ANSI_RESET);
            choice = scanner.nextLine().trim().toUpperCase();

            if (choice.equals("Y")) {
                System.out.println(ANSI_BLUE + "\n--- Recipe Instructions ---" + ANSI_RESET);
                System.out.println(recipe.getInstructions());
            }
        }
    }

    /**
     * Method to view current user's transaction history
     */
    private void viewMyTransactionHistory() {
        System.out.println(ANSI_BLUE + "\n--- My Transaction History ---" + ANSI_RESET);

        transactionHistory.stream()
                .filter(t -> t.contains("User: " + currentUsername))
                .forEach(System.out::println);

        if (transactionHistory.isEmpty()) {
            System.out.println(ANSI_YELLOW + "No transactions found." + ANSI_RESET);
        }
    }

    /**
     * Method for admins to view all transaction history
     * Shows transactions from all users, not just the currently logged-in user
     */
    private void viewAllTransactionHistory() {
        System.out.println(ANSI_BLUE + "\n--- All Transaction History ---" + ANSI_RESET);

        transactionHistory.forEach(System.out::println);

        if (transactionHistory.isEmpty()) {
            System.out.println(ANSI_YELLOW + "No transactions found." + ANSI_RESET);
        }
    }

    /**
     * Method to log a transaction in memory and to file
     */
    private void logTransaction(String username, String actionType, String itemId, String description) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logEntry = String.format("[%s] User: %s | Action: %s | Item: %s | Details: %s",
                timestamp, username, actionType, itemId, description);
        transactionHistory.add(logEntry);
    }
}