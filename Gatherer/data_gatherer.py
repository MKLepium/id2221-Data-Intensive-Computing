import requests
import time
from dotenv import load_dotenv
import os

# Load environment variables from .env file
load_dotenv()

# Get the API key from the environment variable
api_key = os.getenv("API_KEY")
if api_key is None:
    print("API_KEY environment variable not set.")
    exit()

# URL to fetch data from
url = f"https://opendata.straeto.is/bus/{api_key}/status.xml"
print(url)

# Function to fetch and print the XML response
def fetch_and_print_xml():
    try:
        response = requests.get(url)
        if response.status_code == 200:
            # Print the XML response as a string
            print("XML Response:")
            print(response.text)
        else:
            print(f"HTTP request failed with status code {response.status_code}")
    except Exception as e:
        print(f"An error occurred: {str(e)}")

# Configuration
interval_seconds = 10  # Change this to your desired interval
iteration = 0
# Main loop
while True:
    fetch_and_print_xml()
    time.sleep(interval_seconds)
    print("Iteration: " + str(iteration))
    iteration += 1
