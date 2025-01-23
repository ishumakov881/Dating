from flask import Flask, request, jsonify
import os
import random
from dotenv import load_dotenv

load_dotenv()

app = Flask(__name__)

# Демо-база пользователей
USERS = [
    {"id": 1, "name": "Алексей", "age": 28, "city": "Москва"},
    {"id": 2, "name": "Мария", "age": 25, "city": "Санкт-Петербург"},
    {"id": 3, "name": "Дмитрий", "age": 32, "city": "Новосибирск"}
]

@app.route('/')
def home():
    return "💘 Dating API v1.0 💘"

@app.route('/users', methods=['GET'])
def get_users():
    city = request.args.get('city')
    age = request.args.get('age')
    
    filtered_users = USERS
    
    if city:
        filtered_users = [u for u in filtered_users if u['city'].lower() == city.lower()]
    
    if age:
        filtered_users = [u for u in filtered_users if u['age'] <= int(age)]
    
    return jsonify(filtered_users)

@app.route('/match', methods=['GET'])
def get_match():
    return jsonify(random.choice(USERS))

@app.route('/feedback', methods=['POST'])
def receive_feedback():
    data = request.json
    # Здесь будет логика обработки обратной связи
    return jsonify({"status": "success", "message": "Feedback received"})

if __name__ == '__main__':
    port = int(os.environ.get('PORT', 5000))
    app.run(host='0.0.0.0', port=port) 