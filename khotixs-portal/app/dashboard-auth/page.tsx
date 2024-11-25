'use client'
import { useState } from "react";
import axios from "axios";

export default function Dashboard() {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [files, setFiles] = useState([]);
  const [fileName, setFileName] = useState("");

  const apiBaseUrl = "http://localhost:8000/api/v1/images"; // Update with your backend URL.

  // Static JWT token (replace with your actual JWT token)
  const jwtToken = "eyJraWQiOiIzZmIxMWQ2ZC1hM2I1LTQ2M2QtYThiZi1hMmVkNTQ1NGRiNmEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6Im5leHRqczIiLCJuYmYiOjE3MzI1MDg5MDUsInNjb3BlIjpbInVzZXI6ZGVsZXRlIiwiZmlsZTpyZWFkIiwiZmlsZTp3cml0ZSIsIm9wZW5pZCIsInByb2ZpbGUiLCJ1c2VyOnJlYWQiLCJ1c2VyOndyaXRlIiwidXNlcjp1cGRhdGUiLCJlbWFpbCJdLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjkwOTAiLCJleHAiOjE3MzI1OTUzMDUsImlhdCI6MTczMjUwODkwNSwidXVpZCI6IjMyYzNhNjM4LWI1OTMtNDEzYS05ZDc2LTU0ZmEzNTNhZjg3OCIsImp0aSI6ImFkbWluIn0.J9UnE7DZG46j_D6G1wbrpWVDfB96ZYRB_3PVI030TiimLv2XQM7T2sUtrrb73ZOOw1DWoJVNlABjYgiwFjPOQnRzQWOw63OOIW2skGz_szsziQ3Ild5MobIN-cVou2YFfCz0Dkpp82AetVGT7qE0FrpNeTBL31r8-H0XWY5J0K4yBQFqIcWmKqJj8FPPX5zY5tFVQ4NIiCsrJkxa_uruNjIvaYB-n4kMHXtFKT_pYEMZMWKd8N4RCuEbLBXsnuSSzzYeNzXbY-xcrN9kMgocA_bbwMcqDryCcSD9crzEXVbuNdGd42C62lU9OrquUeVXPxDh2Aug3XCidQogbC0jsw";

  // Upload File
  const handleUpload = async () => {
    if (!selectedFile) return alert("Please select a file!");
    const formData = new FormData();
    formData.append("file", selectedFile);

    try {
      const response = await axios.post(apiBaseUrl, formData, {
        headers: { 
          "Content-Type": "multipart/form-data", 
          "Authorization": `Bearer ${jwtToken}` // Attach static JWT token
        },
      });
      alert("File uploaded successfully!");
      console.log(response.data);
    } catch (error) {
      console.error("Error uploading file:", error);
    }
  };

  // Fetch All Files
  const fetchAllFiles = async () => {
    try {
      const response = await axios.get(apiBaseUrl, {
        headers: {
          "Authorization": `Bearer ${jwtToken}` // Attach static JWT token
        }
      });
      setFiles(response.data);
    } catch (error) {
      console.error("Error fetching files:", error);
    }
  };

  // Fetch File by Name
  const fetchFileByName = async () => {
    if (!fileName) return alert("Enter a file name!");
    try {
      const response = await axios.get(`${apiBaseUrl}/${fileName}`, {
        headers: {
          "Authorization": `Bearer ${jwtToken}` // Attach static JWT token
        }
      });
      console.log(response.data);
    } catch (error) {
      console.error("Error fetching file:", error);
    }
  };

  // Delete File by Name
  const deleteFileByName = async () => {
    if (!fileName) return alert("Enter a file name!");
    try {
      const response = await axios.delete(`${apiBaseUrl}/${fileName}`, {
        headers: {
          "Authorization": `Bearer ${jwtToken}` // Attach static JWT token
        }
      });
      alert("File deleted successfully!");
      console.log(response.data);
    } catch (error) {
      console.error("Error deleting file:", error);
    }
  };

  return (
    <div className="flex flex-col items-center w-full h-screen">
      {/* Header Actions */}
      <div className="h-[100px] w-full text-[1.5rem] bg-slate-600 flex justify-around items-center text-white">
        <button onClick={handleUpload}>Upload File</button>
        <button onClick={fetchFileByName}>Get File</button>
        <button onClick={fetchAllFiles}>Get All Files</button>
        <button onClick={deleteFileByName}>Delete File</button>
      </div>

      {/* Content Area */}
      <div className="h-full w-full p-4">
        <div className="flex flex-col items-center gap-4">
          {/* Upload File Input */}
          <div>
            <input
              type="file"
              onChange={(e) => setSelectedFile(e.target.files?.[0] || null)}
              className="mb-2"
            />
          </div>

          {/* Fetch/Delete File Input */}
          <div>
            <input
              type="text"
              placeholder="Enter file name"
              value={fileName}
              onChange={(e) => setFileName(e.target.value)}
              className="border p-2"
            />
          </div>

          {/* Display Fetched Files */}
          {files.length > 0 && (
            <div className="w-full max-w-lg">
              <h2 className="text-lg font-bold mb-2">Files:</h2>
              <ul className="list-disc pl-4">
                {files.map((file, index) => (
                  <li key={index}>{file.name || "Unnamed File"}</li>
                ))}
              </ul>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
