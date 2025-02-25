# Recipe Recommender App

## Overview
Turn leftovers into gourmet meals—powered by AI! This web application allows users to input ingredients they have on hand or upload photos of their pantry to receive personalized recipe recommendations. The application leverages AI to generate recipes based on the provided ingredients, making it easy to create delicious meals from what you already have.

## Features
- **Ingredient Input**: Users can manually enter ingredients they have available.
- **Photo Upload**: Users can upload images of their pantry for ingredient recognition.
- **AI-Powered Recommendations**: The application uses AI to suggest recipes based on the ingredients provided.
- **User-Friendly Interface**: A simple and intuitive web interface for easy navigation.

## Technologies Used
- Java
- Spring Boot
- OpenAI API
- HTML/CSS/JavaScript
- Maven for dependency management

## Setup Instructions
1. **Clone the Repository**:
   ```
   git clone <repository-url>
   cd recipe-recommender-app
   ```

2. **Build the Project**:
   Ensure you have Maven installed, then run:
   ```
   mvn clean install
   ```
   If you encounter an error stating that 'mvn' is not recognized, ensure that Maven is installed and added to your system's PATH environment variable. You can download Maven from [here](https://maven.apache.org/download.cgi) and follow the installation instructions.
   ```

3. **Run the Application**:
   Use the following command to start the application:
   ```
   mvn spring-boot:run
   ```

4. **Access the Application**:
   Open your web browser and navigate to `http://localhost:8080`.

## Usage
- Input your available ingredients in the designated field or upload a photo of your pantry.
- Click on the "Get Recipes" button to receive a list of recommended recipes.
- Explore the recipes and enjoy cooking!

## Contributing
Contributions are welcome! Please submit a pull request or open an issue for any suggestions or improvements.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.