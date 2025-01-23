import os
import logging
from telegram import Update, ReplyKeyboardMarkup, ReplyKeyboardRemove
from telegram.ext import (
    Application, 
    CommandHandler, 
    ContextTypes, 
    ConversationHandler, 
    MessageHandler, 
    filters
)
from faker import Faker
from dotenv import load_dotenv

# Загрузка переменных окружения
load_dotenv()

# Настройка логирования
logging.basicConfig(
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s', 
    level=logging.INFO
)
logger = logging.getLogger(__name__)

# Состояния для ConversationHandler
NAME, AGE, GENDER, CITY, PHOTO = range(5)

# Генератор фейковых данных
fake = Faker('ru_RU')

# Временное хранение анкет
user_profiles = {}
matches = {}

class DatingBot:
    def __init__(self, token):
        self.token = token
        self.application = Application.builder().token(token).build()

    async def start(self, update: Update, context: ContextTypes.DEFAULT_TYPE) -> int:
        """Начало регистрации"""
        await update.message.reply_text(
            "👋 Привет! Давай создадим твою анкету для знакомств. Как тебя зовут?"
        )
        return NAME

    async def save_name(self, update: Update, context: ContextTypes.DEFAULT_TYPE) -> int:
        """Сохраняем имя пользователя"""
        user = update.message.from_user
        user_profiles[user.id] = {"name": update.message.text}
        
        await update.message.reply_text(f"Приятно познакомиться, {update.message.text}! Сколько тебе лет?")
        return AGE

    async def save_age(self, update: Update, context: ContextTypes.DEFAULT_TYPE) -> int:
        """Сохраняем возраст"""
        try:
            age = int(update.message.text)
            if 18 <= age <= 65:
                user = update.message.from_user
                user_profiles[user.id]["age"] = age
                
                keyboard = [['Мужской', 'Женский']]
                reply_markup = ReplyKeyboardMarkup(keyboard, one_time_keyboard=True)
                await update.message.reply_text(
                    "Выбери свой пол:", 
                    reply_markup=reply_markup
                )
                return GENDER
            else:
                await update.message.reply_text("Возраст должен быть от 18 до 65. Попробуй снова.")
                return AGE
        except ValueError:
            await update.message.reply_text("Пожалуйста, введи число.")
            return AGE

    async def save_gender(self, update: Update, context: ContextTypes.DEFAULT_TYPE) -> int:
        """Сохраняем пол"""
        user = update.message.from_user
        user_profiles[user.id]["gender"] = update.message.text
        
        await update.message.reply_text("В каком городе ты живёшь?")
        return CITY

    async def save_city(self, update: Update, context: ContextTypes.DEFAULT_TYPE) -> int:
        """Сохраняем город"""
        user = update.message.from_user
        user_profiles[user.id]["city"] = update.message.text
        
        await update.message.reply_text(
            "Отправь свое фото для анкеты. Это поможет найти больше откликов!",
            reply_markup=ReplyKeyboardRemove()
        )
        return PHOTO

    async def save_photo(self, update: Update, context: ContextTypes.DEFAULT_TYPE) -> int:
        """Сохраняем фото"""
        user = update.message.from_user
        photo_file = await update.message.photo[-1].get_file()
        
        # В реальном приложении нужно сохранять на сервер
        user_profiles[user.id]["photo_id"] = photo_file.file_id
        
        await update.message.reply_text("Анкета создана! 🎉")
        return ConversationHandler.END

    async def generate_match(self, update: Update, context: ContextTypes.DEFAULT_TYPE):
        """Генерируем случайное совпадение"""
        user = update.message.from_user
        
        # Фильтруем анкеты по полу (противоположный)
        current_user = user_profiles.get(user.id, {})
        opposite_gender = 'Женский' if current_user.get('gender') == 'Мужской' else 'Мужской'
        
        potential_matches = [
            profile for uid, profile in user_profiles.items() 
            if uid != user.id and profile.get('gender') == opposite_gender
        ]
        
        if potential_matches:
            match = fake.random.choice(potential_matches)
            
            await update.message.reply_text(
                f"🔥 Твой потенциальный матч:\n\n"
                f"Имя: {match['name']}\n"
                f"Возраст: {match['age']}\n"
                f"Город: {match['city']}"
            )
            
            if 'photo_id' in match:
                await update.message.reply_photo(match['photo_id'])
        else:
            await update.message.reply_text("Пока нет подходящих анкет. Попробуй позже!")

    def run(self):
        """Запуск бота"""
        conv_handler = ConversationHandler(
            entry_points=[CommandHandler('start', self.start)],
            states={
                NAME: [MessageHandler(filters.TEXT & ~filters.COMMAND, self.save_name)],
                AGE: [MessageHandler(filters.TEXT & ~filters.COMMAND, self.save_age)],
                GENDER: [MessageHandler(filters.Regex('^(Мужской|Женский)$'), self.save_gender)],
                CITY: [MessageHandler(filters.TEXT & ~filters.COMMAND, self.save_city)],
                PHOTO: [MessageHandler(filters.PHOTO, self.save_photo)]
            },
            fallbacks=[CommandHandler('cancel', lambda u, c: ConversationHandler.END)]
        )

        self.application.add_handler(conv_handler)
        self.application.add_handler(CommandHandler('match', self.generate_match))

        self.application.run_polling(allowed_updates=Update.ALL_TYPES)

def main():
    token = os.getenv('TELEGRAM_BOT_TOKEN')
    if not token:
        logger.error("Не указан токен Telegram бота!")
        return
    
    bot = DatingBot(token)
    bot.run()

if __name__ == '__main__':
    main() 