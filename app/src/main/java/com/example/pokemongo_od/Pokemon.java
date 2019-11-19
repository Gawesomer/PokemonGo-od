package com.example.pokemongo_od;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Random;

public class Pokemon {

    private int number;
    private LineNumberReader reader;
    private String[] mData;
    private final String catchStateFileName = "catchStateDB.csv";
    private String absFileName;
    FileOutputStream outputStream;

    private enum CatchState {
        UNSEEN, SEEN, CAUGHT
    }

    private CatchState mCatchState;

    public Pokemon(Context context) {
        Random r = new Random();

        // Random number [1-151]
        number = r.nextInt(151)+1;

        construct(context);
    }

    public Pokemon(Context context, int number) {
        this.number = number;

        construct(context);
    }

    private void construct(Context context) {
        try {
            reader = new LineNumberReader(new InputStreamReader(context.getAssets().open("pokemonDB.csv")));
        } catch (IOException e) {
            Log.e("Exception: %s", e.getMessage());
        }

        try {
            while (reader.getLineNumber() != number-1) {
                reader.readLine();
            }
            mData = reader.readLine().split(",");
            reader.close();
        } catch (Exception e) {
            Log.e("Exception: %s", e.getMessage());
        }

        absFileName = context.getFilesDir().getAbsolutePath()+"/"+catchStateFileName;
        File catchStateFile = new File(absFileName);
        //catchStateFile.delete();
        if (!catchStateFile.exists()) {
            try {
                String content = "UNSEEN\n";
                outputStream = context.openFileOutput(catchStateFileName, Context.MODE_PRIVATE);
                for (int i = 1; i <= 151; i++) {
                    outputStream.write(content.getBytes());
                }
                outputStream.close();
            } catch (Exception e) {
                Log.e("Exception: %s", e.getMessage());
            }
            mCatchState = CatchState.UNSEEN;
        } else {
            try {
                reader = new LineNumberReader(new InputStreamReader(new FileInputStream(absFileName)));
            } catch (IOException e) {
                Log.e("Exception: %s", e.getMessage());
            }

            try {
                while (reader.getLineNumber() != number-1) {
                    reader.readLine();
                }
                String catchStateStr = reader.readLine();
                reader.close();
                if (catchStateStr.equals("UNSEEN")) {
                    mCatchState = CatchState.UNSEEN;
                } else if (catchStateStr.equals("SEEN")) {
                    mCatchState = CatchState.SEEN;
                } else {
                    mCatchState = CatchState.CAUGHT;
                }
            } catch (Exception e) {
                Log.e("Exception: %s", e.getMessage());
            }
        }
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    private void writeToCatchStateFile(CatchState state) {
        try {
            reader = new LineNumberReader(new FileReader(absFileName));
            StringBuffer stringBuffer = new StringBuffer();
            String line;

            while (reader.getLineNumber() != number-1) {
                line = reader.readLine();
                stringBuffer.append(line+"\n");
            }
            if (state == CatchState.SEEN) {
                stringBuffer.append("SEEN\n");
            } else if (state == CatchState.UNSEEN) {
                stringBuffer.append("UNSEEN\n");
            } else {
                stringBuffer.append("CAUGHT\n");
            }
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line+"\n");
            }
            reader.close();

            FileOutputStream fileOutputStream = new FileOutputStream(absFileName);
            fileOutputStream.write(stringBuffer.toString().getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void setPokemonSeen() {
        mCatchState = CatchState.SEEN;
        writeToCatchStateFile(mCatchState);
    }

    public void setPokemonCaught() {
        mCatchState = CatchState.CAUGHT;
        writeToCatchStateFile(mCatchState);
    }

    public String getCatchState() {
        if (mCatchState == CatchState.UNSEEN) {
            return "UNSEEN";
        }
        if (mCatchState == CatchState.SEEN) {
            return "SEEN";
        }
        return "CAUGHT";
    }

    public boolean wasSeen() {
        return mCatchState != CatchState.UNSEEN;
    }

    public String getName() {
        return mData[0];
    }
}
