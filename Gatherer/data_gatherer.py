import requests
import time
from dotenv import load_dotenv
import os
from confluent_kafka import Producer

# Load environment variables from .env file
# 
load_dotenv()

# Get the API key from the environment variable
api_key = os.getenv("API_KEY")
if api_key is None:
    print("API_KEY environment variable not set..")
    exit()

# Kafka configuration 
kafka_config = {
    'bootstrap.servers': 'localhost:9092',  # Replace with your Kafka broker(s)
    'client.id': 'xml-producer'
}

# Create a Kafka producer instance
producer = Producer(kafka_config)

# URL to fetch data from
url = f"https://opendata.straeto.is/bus/{api_key}/status.xml"
print(url)

# Function to fetch XML data and push it to Kafka
def fetch_and_push_to_kafka():
    try:
        response = requests.get(url)
        if response.status_code == 200:
            # Push the XML response as a message to a Kafka topic
            producer.produce("xml-data", key=None, value=response.text)
            producer.flush()  # Ensure that the message is sent
            print("XML message pushed to Kafka topic 'xml-messages'")
        else:
            print(f"HTTP request failed with status code {response.status_code}")
    except Exception as e:
        print(f"An error occurred: {str(e)}")

# Configuration
interval_seconds = 10  # Change this to your desired interval
iteration = 0

# Main loop
while True:
    fetch_and_push_to_kafka()
    time.sleep(interval_seconds)
    print("Iteration: " + str(iteration))
    iteration += 1
