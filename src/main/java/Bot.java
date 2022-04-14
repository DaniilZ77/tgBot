import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private double a = 0, b = 0, c = 0, d = 0;
    private boolean isA = false, isB = false, isC = false, isD = false;

    private double parseDouble(String chatId, String s) {
        double n = 0;
        try {
            n = Double.parseDouble(s);
        } catch (Exception e) {
            sendMsg(chatId, "Некорректный ввод");
        }
        return n;
    }

    @Override
    public void onUpdateReceived(Update update) {
        String request = update.getMessage().getText();
        String chatId = update.getMessage().getChatId().toString();
        String message = "";

        if (request.equals("Вычислить точность тахеометра")) {
            double r = Math.sqrt(Math.pow(((2 * 3.14 * d) / 1296) * a, 2) + Math.pow((c / 1000) * d + b, 2));
            a = 0;
            b = 0;
            c = 0;
            d = 0;
            sendMsg(chatId, String.format("%(.2f", r) + " мм");
        }

        if (isA) {
            a = parseDouble(chatId, request);
            isA = false;
            return;
        } else if (isB) {
            b = parseDouble(chatId, request);
            isB = false;
            return;
        } else if (isC) {
            c = parseDouble(chatId, request);
            isC = false;
            return;
        } else if (isD) {
            d = parseDouble(chatId, request);
            isD = false;
            return;
        }

        if (request.equals("Угловая точность тахеометра")) {
            message = "Введите угловую точность тахеометра в секундах:";
            isA = true;
        } else if (request.equals("Точность дальномера")) {
            message = "Введите точность дальномера в милиметрах:";
            isB = true;
        } else if (request.equals("PPM дальномера тахеометра")) {
            message = "Введите PPM дальномера тахеометра в милиметрах:";
            isC = true;
        } else if (request.equals("Расстояние до цели")) {
            message = "Введите расстояние до цели в метрах:";
            isD = true;
        }
        if (isA || isB || isC || isD) {
            sendMsg(chatId, message);
        }
    }

    public void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        setButtons(sendMessage);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Угловая точность тахеометра"));
        keyboardFirstRow.add(new KeyboardButton("Точность дальномера"));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("PPM дальномера тахеометра"));
        keyboardSecondRow.add(new KeyboardButton("Расстояние до цели"));

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(new KeyboardButton("Вычислить точность тахеометра"));

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    @Override
    public String getBotUsername() {
        return "takhebot";
    }

    @Override
    public String getBotToken() {
        return "5392428860:AAHiYiRKf_uiZaZBgLmVvPPZ60yJXIohSjk";
    }
}
