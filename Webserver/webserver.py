from flask import Flask, request, jsonify
import psycopg2

app = Flask(__name__)

# PostgreSQL database configuration
db_params = {
    'dbname': 'bus_data',
    'user': 'postgres',
    'password': 'db_password',
    'host': '127.0.0.1',
    'port': '5432'
}

# Define your database query function  
def get_data_from_db():
    conn = psycopg2.connect(**db_params)
    cur = conn.cursor()
    # Returns the fer, route, lat and lon of all buses (potentially only buses with route 1-36 if we filter that in the backend, but we could also do it in the frontend no problem)
    # filter for only busses with route 1-36
    cur.execute("SELECT fer, route, lat, lon FROM bus_data_schema.bus_data")

    data = cur.fetchall()
    cur.close()
    conn.close()
    return data

# Endpoint to retrieve data
@app.route('/bus?getData', methods=['GET'])
def get_bus_data():
    try:
        data = get_data_from_db()
        return jsonify({"data": data})
    except Exception as e:
        return jsonify({"error": str(e)})

if __name__ == '__main__':
    app.run(debug=True)
