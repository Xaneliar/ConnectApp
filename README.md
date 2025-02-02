---

**Project Description:**

**CONNECT** is an innovative location-based mobile application that simplifies event and movie discovery for users. The app automatically identifies the user's city and provides curated recommendations for local events, such as concerts, theater performances, and film screenings. With seamless integration with ticket booking platforms like BookMyShow, it ensures users can easily reserve tickets to their favorite activities.  

The application is developed using Java and MySQL for efficient data management, featuring a secure user authentication system that includes OTP-based login for enhanced security. Users can share feedback through testimonials and enjoy personalized content tailored to their location.

---

**README.md:**

```markdown
# CONNECT - Local Event Discovery Application  

## Project Overview  
CONNECT is a mobile application designed to help users discover local events and movies effortlessly. By leveraging geolocation services, the app provides personalized event listings, allowing users to find concerts, shows, and social events happening nearby. With integration for ticket booking and user feedback, CONNECT aims to be the ultimate entertainment discovery tool.  

## Features  
- **Location-Based Event Discovery:** Automatically detects user location to provide relevant event suggestions.  
- **Event Categories:** Organized listings for movies, live music, stand-up comedy, workshops, and more.  
- **Ticket Booking Integration:** Seamless redirection to BookMyShow for ticket purchases.  
- **User Testimonials:** Community-driven reviews help users make informed decisions.  
- **Secure Login:** OTP-based verification for enhanced security during user and admin logins.  

## Technical Details  
- **Programming Language:** Java  
- **Frontend:** Java Swing  
- **Backend:** MySQL  
- **APIs Used:**  
  - IPinfo API for location detection  
  - Twilio API for OTP verification  
  - BookMyShow for ticket booking  

## Installation Guide  
1. **Clone the Repository:**  
   ```bash
   git clone <repository_url>
   cd CONNECT-App
   ```  
2. **Set Up Database:**  
   - Install MySQL and import the provided SQL script to create necessary tables.  
   - Update database connection configurations in the `DatabaseConnection.java` file.  

3. **Run the Application:**  
   - Compile and execute the main class `ConnectEventsApp.java` using a Java IDE or terminal:  
   ```bash
   javac ConnectEventsApp.java  
   java ConnectEventsApp  
   ```  

## Usage  
1. Register or log in with OTP-based authentication.  
2. Discover events based on your location.  
3. View event details and book tickets seamlessly.  
4. Share and read testimonials from other users.  

## Security Features  
- Password hashing for secure storage  
- Encrypted API communication using SSL/TLS  
- OTP-based authentication for user and admin accounts  

## Future Enhancements  
- AI-powered event recommendations  
- Social sharing features  
- Push notifications for event reminders  
- Integration with additional ticketing platforms  

## Contributors  
- Aditya Nikhoria  
- B. Tejashwin  
- Gautam Soni  
- Surada Vaishnavi  

## Acknowledgments  
Special thanks to Dr. Jagadeesh M and the Department of Computing Technologies, SRM Institute of Science and Technology, for their guidance and support.

## License  
[MIT License](LICENSE)  
```
