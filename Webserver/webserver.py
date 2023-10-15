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
WITH recent_buses AS (
    SELECT
        *,
        RANK() OVER(PARTITION BY dev ORDER BY time DESC) AS rnk
    FROM
        bus_data_schema.bus_data
    WHERE
        time >= NOW() - INTERVAL '2 hours' - INTERVAL '10 minutes'
)
SELECT
    time,
    fer,
    route,
    lat,
    lon,
    next
FROM
    recent_buses
WHERE
    rnk = 1;
    """)

    data = cur.fetchall()

    filtered_data = []
    for row in data:
        try:
            # Assuming route is the third column, convert it to integer and check if it's between 0 and 36
            if 0 <= int(row[2]) <= 36:
                filtered_data.append(row)
        except ValueError:
            filtered_data.append(row)
            pass

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
