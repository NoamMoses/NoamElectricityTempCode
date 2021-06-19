package com.noam.noamelectricity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Random;


public class MainPage extends Fragment {
    /*
    * Main page - and NOT MainActivity
    * */

    /*
    * --------------------
    * | System functions |
    * --------------------
    * */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*
        * onCreateView
        * --------------
        * Being called when:
        * 1) The app is first up - before onViewCreated
        * 2) When you rotate the screen - before onViewCreated
        * 3) When you come back to this activity, from a different activity - and onViewCreated IS NOT being called after it
        * */

        if (savedInstanceState != null) { // After rotating the screen
            initARR();
            // Without this line I would get null (array of quotes) when coming back to this activity from another activity
            // -A button click (generate new quote) will fix it, but this line "does the click" for me
        }

        // else-statement would be whenever I open the fragment, after a different fragment was open
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        /*
         * Being called whenever you rotate the screen
         * NOT being called when you switch between activities - not when you leave the activity and not when you open it again
         * */

        outState.putInt("count1", quotes_programming_counter);
        outState.putInt("count2", quotes_general_counter);
        super.onSaveInstanceState(outState);
    }


    /*
     * -----------
     * | My code |
     * -----------
     * */

    String[][][] quotes = new String[4][500][2];
    /*
    * All of the quotes:
    * 4 categories (2 used - programming and general)
    * 500 quotes in each category
    * 2 creators
    * */
    static int quotes_programming_counter = 0, quotes_general_counter = 0, //  How many quotes there are - to know the limit of the random number generator
            lastRand = 0; // To make sure there aren't two same random numbers one after the other
    final int CAT_PROGRAMMING = 0, CAT_GENERAL = 1; // Just for myself, a "#define"-style value, to indicate the category
    Random rand = new Random(); // Random number generator, for getting a random quote index each time

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        /*
        * onViewCreated
        * --------------
        * Being called after onCreateView, and when:
        * 1) The app is first up
        * 2) When you rotate the screen
        * */

        /* Init the bottom part - last updated & version */
        ((TextView)view.findViewById(R.id.home_info)).setText(String.format(getResources().getString(R.string.lastUpdate), getResources().getString(R.string.lastUpdateDate)));

        initARR(); // Init the quotes - and will be later "overridden", every time the fragment is being re-created - e.g when rotating the screen

        // Buttons to generate a new quote
        Button buttonGenNewQ_programming = view.findViewById(R.id.genNewQuote_programming),
                buttonGenNewQ_general = view.findViewById(R.id.genNewQuote_general);

        /*
        * "Quote of the day"
        * */
        TextView QD_quote = view.findViewById(R.id.QD_quote),
                QD_quoteBy = view.findViewById(R.id.QD_quoteBy); // Quote itself, and the person's name
        QD_quote.setBackgroundResource(R.drawable.border_style1); // A nice fancy border to the quote
        /* Chooses a random quote for when the app is first up */
        String[] currentQuote = quotes[0][rand.nextInt(quotes_programming_counter)]; // Just for the first time, when the app is up, will always be cat 0 - programming quote
        String currentQuote_text = currentQuote[0], currentQuote_by = currentQuote[1]; // The generated quote
        /* String-builders just so the IDE won't annoy me and ask me to use string templates */
        QD_quote.setText(String.format(getResources().getString(R.string.quotedText_template), currentQuote_text));
        QD_quoteBy.setText(String.format("- %1$s", currentQuote_by));
        
        // Generating new quote on button-click
        buttonGenNewQ_programming.setOnClickListener(v -> generateNewQuote(CAT_PROGRAMMING, QD_quote, QD_quoteBy));
        buttonGenNewQ_general.setOnClickListener(v -> generateNewQuote(CAT_GENERAL, QD_quote, QD_quoteBy));
    }

    void generateNewQuote(int category, TextView quote, TextView by) {
        /*
         * generateNewQuote
         * -----------------
         * Explanation:
         *   The function generates a new random quote of the requested category and sets the TextViews' values
         * Parameters:
         *   int category:
         *     Which category the quote is linked to
         *     Expected values: 0/1 - indicator which quotes category is needed - 0=programming, 1=general
         *   TextView quote: TextView of the quote itself
         *   TextView by: TextView of the person's name
         * */
        
        int max = 0, // The limit of each category
                currentRand; // Current random number
        if (category == 0) { // Programming quote
            max = quotes_programming_counter;
        } else if (category == 1) { // General quote
            max = quotes_general_counter;
        }
        currentRand = rand.nextInt(max);
        
        /* This loop is for making sure that there isn't the same random number twice */
        while (currentRand == lastRand) {
            currentRand = rand.nextInt(max);
        }
        
        /* Outside the loop, means the index is not twice the same */
        lastRand = currentRand;
        String[] currentQuote = quotes[category][currentRand];
        String currentQuote_text = currentQuote[0], currentQuote_by = currentQuote[1]; // The current quote
        quote.setText(String.format(getResources().getString(R.string.quotedText_template), currentQuote_text));
        by.setText(String.format("- %1$s", currentQuote_by));
    }

    void addQuote(int category, String quote, String by) {
        /*
        * addQuote
        * ----------
        * Explanation:
        * The function saves a new quote into the quotes array list.
        *   Index 0: The quote itself
        *   Index 1: The person's name
        *   The function also increases the static counter variable - therefore this variable is being reset in initARR
        * Parameters:
         *   int category:
         *     Which category the quote is linked to
         *     Expected values: 0/1 - indicator which quotes category is needed - 0=programming, 1=general
        *   String quote: the quote itself
        *   String by: The person's name
        * */
        if (category == CAT_PROGRAMMING) {
            quotes[0][quotes_programming_counter][0] = quote;
            quotes[0][quotes_programming_counter][1] = by;
            quotes_programming_counter++;
        } else if (category == CAT_GENERAL) {
            quotes[1][quotes_general_counter][0] = quote;
            quotes[1][quotes_general_counter][1] = by;
            quotes_general_counter++;
        }
    }

    void initARR() {
        /*
         * initARR
         * ---------
         * Explanation:
         *   The function saves all of the quotes, according to the following algorithm:
         *     index 0 - category (0=programming, 1=general)
         *     index 1 - quote itself
         *     index 2 - the person's name
         *   The function also reset the counters because it will anyways re-count the items - every call to addQuote.
         *     Otherwise, the counter will increase each time, by the number of the last size.
         *     For example: if programming_counter was 15 when the app first open (means first call to initARR),
         *       then each call to initARR would increase it by 15 - 15, 30, 45, etc..
         *
         * * The quotes are separated into groups of 5 just to make it easier to read and count
         */
        quotes_programming_counter = 0;
        quotes_general_counter = 0;

        /*
        * ---------------------
        * | Programming quotes |
        * ---------------------
        * */
        addQuote(CAT_PROGRAMMING, "I think everybody in this country should learn how to program. Because it teaches you how to think.", "Steve Jobs");
        addQuote(CAT_PROGRAMMING, "In some ways, programming is like painting. You start with a blank canvas and certain basic raw materials. You use a combination of science, art, and craft to determine what to do with them.", "Andrew Hunt");
        addQuote(CAT_PROGRAMMING, "The best error message is the one that never shows up.", "Thomas Fuchs");
        addQuote(CAT_PROGRAMMING, "Don't write better error messages, write code that doesn't need them.", "Jason C. McDonald");
        addQuote(CAT_PROGRAMMING, "Programming isn't about what you know; it's about what you can figure out.", "Chris Pine");

        addQuote(CAT_PROGRAMMING, "The best error message is the one that never shows up.", "Thomas Fuchs");
        addQuote(CAT_PROGRAMMING, "Any fool can write code that a computer can understand. Good programmers write code that humans can understand.", "Martin Fowler");
        addQuote(CAT_PROGRAMMING, "Experience is the name everyone gives to their mistakes.", "Oscar Wilde");
        addQuote(CAT_PROGRAMMING, "Java is to JavaScript what car is to Carpet.", "Chris Heilmann");
        addQuote(CAT_PROGRAMMING, "Sometimes it pays to stay in bed on Monday, rather than spending the rest of the week debugging Monday’s code.", "Dan Salomon");

        addQuote(CAT_PROGRAMMING, "Code is like humour. When you have to explain it, it’s bad.", "Cory House");
        addQuote(CAT_PROGRAMMING, "Fix the cause, not the symptom.", "Steve Maguire");
        addQuote(CAT_PROGRAMMING, "Optimism is an occupational hazard of programming: feedback is the treatment.", "Kent Beck");
        addQuote(CAT_PROGRAMMING, "Simplicity is the soul of efficiency.", "Austin Freeman");
        addQuote(CAT_PROGRAMMING, "Before software can be reusable it first has to be usable.", "Ralph Johnson");

        addQuote(CAT_PROGRAMMING, "Most good programmers do programming not because they expect to get paid or get adulation by the public, but because it is fun to program.", "Linus Torvalds");
        addQuote(CAT_PROGRAMMING, "Intelligence is the ability to avoid doing work, yet getting the work done.", "Linus Torvalds");
        addQuote(CAT_PROGRAMMING, "Any program is only as good as it is useful.", "Linus Torvalds");
        addQuote(CAT_PROGRAMMING, "Design is a funny word. Some people think design means how it looks. But of course, if you dig deeper, it's really how it works.", "Steve Jobs");



        /*
         * ------------------
         * | General quotes |
         * ------------------
         * */
        addQuote(CAT_GENERAL, "We cannot solve our problems with the same thinking we used when we created them.", "Albert Einstein");
        addQuote(CAT_GENERAL, "A person who never made a mistake never tried anything new.", "Albert Einstein");
        addQuote(CAT_GENERAL, "If you can't explain it simply, you don't understand it well enough.", "Albert Einstein");
        addQuote(CAT_GENERAL, "Learn from yesterday, live for today, hope for tomorrow. The important thing is not to stop questioning.", "Albert Einstein");
        addQuote(CAT_GENERAL, "Look deep into nature, and then you will understand everything better.", "Albert Einstein");

        addQuote(CAT_GENERAL, "Life is like riding a bicycle. To keep your balance, you must keep moving.", "Albert Einstein");
        addQuote(CAT_GENERAL, "Only two things are infinite, the universe and human stupidity, and I'm not sure about the former.", "Albert Einstein");
        addQuote(CAT_GENERAL, "Logic will get you from A to B. Imagination will take you everywhere.", "Albert Einstein");
        addQuote(CAT_GENERAL, "I have no special talent. I am only passionately curious.", "Albert Einstein");
        addQuote(CAT_GENERAL, "It's not that I'm so smart, it's just that I stay with problems longer.", "Albert Einstein");

        addQuote(CAT_GENERAL, "Coincidence is God's way of remaining anonymous.", "Albert Einstein");
        addQuote(CAT_GENERAL, "The true sign of intelligence is not knowledge but imagination.", "Albert Einstein");
        addQuote(CAT_GENERAL, "Only a life lived for others is a life worthwhile.", "Albert Einstein");
        addQuote(CAT_GENERAL, "Education is what remains after one has forgotten what one has learned in school.", "Albert Einstein");
        addQuote(CAT_GENERAL, "You can't blame gravity for falling in love.", "Albert Einstein");

        addQuote(CAT_GENERAL, "Weakness of attitude becomes weakness of character.", "Albert Einstein");
        addQuote(CAT_GENERAL, "The only source of knowledge is experience.", "Albert Einstein");
        addQuote(CAT_GENERAL, "Peace cannot be kept by force; it can only be achieved by understanding.", "Albert Einstein");
        addQuote(CAT_GENERAL, "Try not to become a man of success, but rather try to become a man of value.", "Albert Einstein");
        addQuote(CAT_GENERAL, "Reality is merely an illusion, albeit a very persistent one.", "Albert Einstein");

        addQuote(CAT_GENERAL, "Imagination is everything. It is the preview of life's coming attractions.", "Albert Einstein");
        addQuote(CAT_GENERAL, "If we knew what it was we were doing, it would not be called research, would it?", "Albert Einstein");
        addQuote(CAT_GENERAL, "Testing leads to failure, and failure leads to understanding.", "Burt Rutan");
        addQuote(CAT_GENERAL, "In order to be irreplaceable, one must always be different.", "Coco Chanel");
        addQuote(CAT_GENERAL, "Make it work, make it right, make it fast.", "Kent Beck");

        addQuote(CAT_GENERAL, "If you don't like something, change it. If you can't change it, change your attitude.", "Maya Angelou");
        addQuote(CAT_GENERAL, "Great things in business are never done by one person. They're done by a team of people.", "Steve Jobs");
        addQuote(CAT_GENERAL, "Life is 10% what happens to you and 90% how you react to it.", "Charles R. Swindoll");
        addQuote(CAT_GENERAL, "Keep your face always toward the sunshine - and shadows will fall behind you.", " Walt Whitman");
        addQuote(CAT_GENERAL, "Sometimes when you innovate, you make mistakes. It is best to admit them quickly, and get on with improving your other innovations.", "Steve Jobs");

        addQuote(CAT_GENERAL, "Research is what I'm doing when I don't know what I'm doing.", "Wernher von Braun");
        addQuote(CAT_GENERAL, "What is research but a blind date with knowledge?", "Will Harvey");
        addQuote(CAT_GENERAL, "If you steal from one author, it's plagiarism; if you steal from many, it's research.", "Wilson Mizner");
        addQuote(CAT_GENERAL, "Research is creating new knowledge.", "Neil Armstrong");
        addQuote(CAT_GENERAL, "The best preparation for tomorrow is doing your best today.", "H. Jackson Brown, Jr.");

        addQuote(CAT_GENERAL, "The heart and soul of good writing is research; you should write not what you know but what you can find out about.", "Robert J. Sawyer");
        addQuote(CAT_GENERAL, "What people actually refer to as research nowadays is really just Googling.", "Dermot Mulroney");
        addQuote(CAT_GENERAL, "Research is to see what everybody else has seen, and to think what nobody else has thought.", "Albert Szent-Gyorgyi");
        addQuote(CAT_GENERAL, "If you can't describe what you are doing as a process, you don't know what you're doing.", "W. Edwards Deming");
        addQuote(CAT_GENERAL, "The best and most beautiful things in the world cannot be seen or even touched - they must be felt with the heart.", "Helen Keller");

        addQuote(CAT_GENERAL, "I hated every minute of training, but I said, 'Don't quit. Suffer now and live the rest of your life as a champion.'", "Muhammad Ali");
        addQuote(CAT_GENERAL, "What lies behind you and what lies in front of you, pales in comparison to what lies inside of you.", "Ralph Waldo Emerson");
        addQuote(CAT_GENERAL, "Don't judge each day by the harvest you reap but by the seeds that you plant.", "Robert Louis Stevenson");
        addQuote(CAT_GENERAL, "You can't just ask customers what they want and then try to give that to them. By the time you get it built, they'll want something new.", "Steve Jobs");
        addQuote(CAT_GENERAL, "Older people sit down and ask, 'What is it?' but the boy asks, 'What can I do with it?'.", "Steve Jobs");

        addQuote(CAT_GENERAL, "Things don't have to change the world to be important.", "Steve Jobs");
    }
}