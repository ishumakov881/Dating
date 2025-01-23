FROM python:3.10-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY src/ .

EXPOSE 5000

CMD exec gunicorn --bind 0.0.0.0:${PORT:-5000} app:app 