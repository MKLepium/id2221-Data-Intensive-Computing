from flask import Flask, request, jsonify
from flask import send_from_directory
from flask_cors import CORS
import psycopg2

app = Flask(__name__)
CORS(app)

# PostgreSQL database configuration
db_params = {
    'dbname': 'bus_data',
    'user': 'postgres',
    'password': 'db_password',
    'host': '127.0.0.1',
    'port': '5432'
}

# Define your database query function  
def get_latest_bus_data_from_db():
    conn = psycopg2.connect(**db_params)
    cur = conn.cursor()
    # Returns the fer, route, lat and lon of all buses (potentially only buses with route 1-36 if we filter that in the backend, but we could also do it in the frontend no problem)
    # filter for only busses with route 1-36
    # Hopefully this works
    cur.execute("""
    SELECT 
        b.time, 
        b.fer, 
        b.route, 
        b.lat, 
        b.lon,
        b.next
    FROM 
        bus_data_schema.bus_data b
    WHERE 
        (b.route, b.time) IN (
            SELECT 
                route, 
                MAX(time)
            FROM 
                bus_data_schema.bus_data
            WHERE 
                CAST(SUBSTRING(fer, '^\d+') AS INTEGER) >= 0
                AND CAST(SUBSTRING(fer, '^\d+') AS INTEGER) <= 36
                AND time > CURRENT_TIMESTAMP - INTERVAL '2 hours 10 minutes'
            GROUP BY 
                route
        )
        AND b.time > CURRENT_TIMESTAMP - INTERVAL '2 hours 10 minutes';

    """)
    data = cur.fetchall()
    cur.close()
    conn.close()
    return data

# Endpoint to retrieve data
@app.route('/bus/getData', methods=['GET'])
def get_bus_data():
    try:
        data = get_latest_bus_data_from_db()
        return jsonify({"data": data})
    except Exception as e:
        return jsonify({"error": str(e)})

@app.route('/')
def index():
    return "Hello, World!"

@app.route('/<path:filename>.html')
def serve_html(filename):
    return send_from_directory('Frontend', f'{filename}.html')

@app.route('/<path:filename>.js')
def serve_js(filename):
    return send_from_directory('Frontend', f'{filename}.js')

@app.route('/styles/<path:filename>.css')
def serve_styles_css(filename):
    return send_from_directory('Frontend/styles', f'{filename}.css')


@app.route('/<path:path>')
def serve_gtfs_data(path):
    return send_from_directory('Frontend/gtfs-data', path)

if __name__ == '__main__':
    app.run(debug=True, port=8080, host='0.0.0.0')
