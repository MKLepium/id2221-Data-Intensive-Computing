import requests
import time
import sys
from dotenv import load_dotenv
import os
from confluent_kafka import Producer
import logging
import xml.etree.ElementTree as ET


logging.basicConfig(level=logging.DEBUG)

# Load environment variables from .env file
if os.path.exists(".env"):
    load_dotenv() 
# Get API key from environment variable

 
api_key = os.getenv("API_KEY")
if api_key is None:
    print("API_KEY environment variable not set..") 
    exit() 

# Kafka configuration 
kafka_config = {
    'bootstrap.servers': 'localhost:9092', 
    'client.id': 'xml-producer'
}

# Create a Kafka producer instance

# URL to fetch data from
url = f"https://opendata.straeto.is/bus/{api_key}/status.xml"
print(url)

def fetch_data(url, timeout):
    try:
        response = requests.get(url, timeout=timeout)
        response.raise_for_status()
        xml_content = response.text
        logging.debug(f"XML content: {xml_content}")
        try:
            # Attempt to parse the XML to check for proper formatting
            root = ET.fromstring(xml_content)
            return xml_content
        except ET.ParseError as e:
            logging.warning(f"XML parsing error: {str(e)}")
            return None
    except requests.RequestException as e:
        logging.warning(f"Request failed: {str(e)}")
        return None

def push_to_kafka(data, topic, kafka_config):
    producer = Producer(**kafka_config)
    try:
        # Push the XML data to Kafka
        producer.produce(topic, value=data)
        producer.flush()
    except Exception as e:
        logging.error(f"Failed to push data to Kafka: {str(e)}")

def fetch_and_push_to_kafka(url, kafka_config, retries=3, timeout=15, topic="xml-data"):
    for _ in range(retries):
        data = fetch_data(url, timeout)
        if data is not None:
            push_to_kafka(data, topic, kafka_config)
            return True
    return False


# Configuration
interval_seconds = 10
iteration = 0

# Main loop
while True:
    if fetch_and_push_to_kafka(url, kafka_config): 
        print("Successfully fetched and pushed data to Kafka")
    else:
        print("Failed to fetch and push data to Kafka")

    time.sleep(interval_seconds)
    print("Iteration: " + str(iteration))
    sys.stdout.flush() # Why is it not printing :c
    sys.stderr.flush() # Why is it not printing :c
    iteration += 1
