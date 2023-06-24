from fastapi import FastAPI, File, UploadFile, Response
import os
from bson import ObjectId
from pymongo import MongoClient
import uvicorn
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI()

origins = ["*"]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

client = MongoClient("mongodb://localhost:27017")
db = client['iiiteverything']
collection = db["savedFiles"]
editors = db["editor"]
admin = db["admin"]


# Uploading File function
@app.post("/upload")
async def func(sub: str, docType: str, uploader: int, file: UploadFile = File(...)):
    try:
        # Save the file to the local directory
        file_path = f"./uploaded/{file.filename}"
        with open(file_path, "wb") as local_file:
            local_file.write(await file.read())

        # Store file details in MongoDB
        file_info = {
            "filename": file.filename,
            "content_type": file.content_type,
            "size": os.path.getsize(file_path),
            "path": file_path,
            "sub": sub,
            "documentType": docType,
            "uploader": uploader
        }
        result = collection.insert_one(file_info)

        return {"file_id": str(result.inserted_id)}
    except Exception as e:
        return {"message": "An error occurred while uploading the file.", "error": str(e)}


# Downloading file function
@app.get("/download/{file_id}")
async def download_file(file_id: str):
    try:
        file_info = collection.find_one({"_id": ObjectId(file_id)})
        if file_info:
            file_path = file_info["path"]
            with open(file_path, "rb") as file:
                contents = file.read()

            response = Response(
                content=contents, media_type=file_info["content_type"])
            response.headers["Content-Disposition"] = f"attachment; filename={file_info['filename']}"
            return response
        else:
            return {"message": "File not found."}
    except Exception as e:
        return {"message": "An error occurred while downloading the file.", "error": str(e)}


# Deleting File function
@app.get("/delete/{file_id}")
def delete_file(file_id: str):
    try:
        file_info = collection.find_one({"_id": ObjectId(file_id)})
        if file_info:
            file_path = file_info["path"]
            os.remove(file_path)
            query = {'_id': ObjectId(file_id)}
            result = collection.delete_one(query)
            if result.deleted_count > 0:
                return {"deleted file"}
            else:
                return {"file not found"}
    except Exception as e:
        return {"message": "An error occurred while deleting the file.", "error": str(e)}


# Finding files function
@app.get("/find")
async def find(sub: str = "", docType: str = ""):
    query = {}
    if sub:
        query['sub'] = sub
    if docType:
        query['docType'] = docType
    entries = collection.find(query)
    results = []
    for entry in entries:
        entry['_id'] = str(entry['_id'])
        results.append(entry)
    return results


# authenticate the editor
@app.post("/addeditor")
async def addeditor(id: int, password: str, name: str, adder: int):
    try:
        editor_info = {
            "name": name,
            "studentid": id,
            "password": password,
            "adder": adder
        }
        result = editors.insert_one(editor_info)
        return {"added": str(result.__inserted_id)}
    except Exception as e:
        return {"message": "An error occurred while adding the editor", "error": str(e)}


# authenticate the admin
@app.post("/addadmin")
async def addadmin(id: int, password: str, name: str, masterpass: str):
    try:
        if (masterpass == "Swoyam@121065"):
            admin_info = {
                "name": name,
                "studentid": id,
                "password": password,
                "adder": 121065
            }
            result = admin.insert_one(admin_info)
            return {"added": str(result.__inserted_id)}
        else:
            return {"Wrong master pass"}
    except Exception as e:
        return {"message": "An error occurred while adding the editor", "error": str(e)}


# checking the editor
@app.get("/checkeditor")
async def check(id: int, password: str):
    try:
        query = {}
        query['studentid'] = id
        query['password'] = password
        found = editors.find(query)
        if found > 0:
            return True
        else:
            return False
    except Exception as e:
        return {"message": "An error occurred while finding the editor", "error": str(e)}


# checking the admin
@app.get("/checkadmin")
async def check(id: int, password: str):
    try:
        query = {}
        query['studentid'] = id
        query['password'] = password
        found = admin.find(query)
        if found > 0:
            return True
        else:
            return False
    except Exception as e:
        return {"message": "An error occurred while finding the editor", "error": str(e)}


# Getting the name of subjects
@app.get("/getsubjects")
async def getsubjects():
    try:
        unique_sub_values = collection.distinct("sub")
        return {"unique_sub_values": unique_sub_values}
    except Exception as e:
        return {"message": "An error occurred while finding the subjects", "error": str(e)}

# Checking connection


@app.get("/")
async def root():
    return {"message": "Everything is working"}

# Starting the server
if __name__ == "__main__":
    uvicorn.run(app, host="127.0.0.1", port=3000)
