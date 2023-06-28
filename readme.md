# IIIT Everything

IIIT Everything is a web application that allows users to upload and manage various resources related to the curriculum of your institute. It provides a solution for the limited storage capacity of Google Drive by allowing users to store files locally and manage them using a MongoDB database.

## IIIT Everything API

The IIIT Everything API is a FastAPI-based backend that provides the necessary endpoints to upload, download, delete, and search files. It also handles user authentication for editors and administrators.

### API Documentation

The API documentation can be accessed using the following link: [IIIT Everything API Documentation]

### Swagger UI

The Swagger UI provides an interactive interface for exploring and testing the API endpoints. You can access the Swagger UI using the following link: [IIIT Everything Swagger UI]

## IIIT Everything Web App

The IIIT Everything web app is a front-end application built with React that provides an intuitive and user-friendly interface for interacting with the IIIT Everything API. It allows users to upload files, search for resources based on subject and document type, and manage their uploaded files.

## Features

- Upload and save PDF and other document files.
- Store file details such as filename, content type, size, uploader, and upload date in MongoDB.
- Retrieve files by searching through the MongoDB database.
- Download files from the local directory.
- Delete files and remove their entries from the database.
- Authenticate editors and administrators.
- Intuitive user interface for seamless interaction.

## Technologies Used

- Python: The backend of the application is written in Python.
- FastAPI: A modern, fast (high-performance), web framework for building APIs with Python.
- MongoDB: A NoSQL document database used for storing file details.
- PyMongo: A Python library for working with MongoDB.
- FastAPI middleware for CORS: Allows cross-origin resource sharing for web applications.
- Uvicorn: A lightning-fast ASGI server.
- React: A JavaScript library for building user interfaces.
- HTML, CSS, JavaScript: The frontend of the IIIT Everything web app is built using these web technologies.

## Prerequisites

Before running the application, ensure you have the following installed:

- Node.js (version 14 or later)
- Python (version 3.8 or later)
- MongoDB
- Required Python packages: `fastapi`, `pymongo`, `uvicorn`
- Required Node.js packages: `axios`, `react`, `react-dom`, `react-scripts`

## Getting Started

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/iiit-everything.git
   ```

2. Change to the project directory:

   ```bash
   cd iiit-everything
   ```

3. Install the required Python packages:

   ```bash
   pip install -r requirements.txt
   ```
4. Start the backend server:

   ```bash
   uvicorn main:app --reload
   ```

   The backend server will be running at `http://localhost:8000`.
5. Install the required Node.js packages:

   ```bash
   cd iiiteverythingwebsite
   npm install
   ```

6. Start the frontend development server:

   ```bash
   npm start
   ```

   The frontend development server will be running at `http://localhost:3000`.

7. Open your web browser and access the IIIT Everything web app at `http://localhost:3000`.

### API Endpoints

- `POST /upload`: Uploads a file to the server.
- `GET /download/{file_id}`: Downloads a file by its ID.
- `GET /delete/{file_id}`: Deletes a file by its ID.
- `GET /find`: Searches for files based on subject and document type.
- `POST /addeditor`: Authenticates and adds an editor.
- `POST /addadmin`: Authenticates and adds an admin.
- `GET /checkeditor`: Checks if an editor is valid.
- `GET /checkadmin`: Checks if an admin is valid.
- `GET /getsubjects`: Retrieves the names of subjects.

For detailed documentation and to interact with the API, please refer to the [IIIT Everything API Documentation](https://dbiiit.swoyam.engineer/docs) and explore the Swagger UI.

### Configuration

You can modify the following configurations in the `main.py` file:

- MongoDB connection: Update the connection string (`mongodb://localhost:27017`) if your MongoDB server is running on a different host or port.
- Database and collection names: Update the values of `db_name`, `collection_name`, `editors_name`, and `admin_name` variables to match your MongoDB setup.

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please feel free to open an issue or submit a pull request. Make sure to follow the existing code style and guidelines.

1. Fork the repository.
2. Create a new branch: `git checkout -b my-feature`.
3. Make your changes and commit them: `git commit -m 'Add some feature'`.
4. Push to the branch: `git push origin my-feature`.
5. Open a pull request.

Please make sure to update tests as appropriate and provide a detailed description of your changes.


## License

This project is licensed under the [MIT License](LICENSE).
