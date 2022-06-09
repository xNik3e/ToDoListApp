package com.example.todoplaceholder.utils.databaseUtils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.todoplaceholder.models.CategoryModel;
import com.example.todoplaceholder.models.TaskModel;
import com.example.todoplaceholder.utils.databaseUtils.converters.CategoryModelConverter;
import com.example.todoplaceholder.utils.databaseUtils.converters.DateConverter;
import com.example.todoplaceholder.utils.databaseUtils.converters.StringListConverter;
import com.example.todoplaceholder.utils.databaseUtils.daos.CategoryDao;
import com.example.todoplaceholder.utils.databaseUtils.daos.TaskDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {CategoryModel.class, TaskModel.class},
        version = 1
)
@TypeConverters({
        DateConverter.class,
        CategoryModelConverter.class,
        StringListConverter.class})
public abstract class MyRoomDatabase extends RoomDatabase {

    public abstract CategoryDao categoryDao();

    public abstract TaskDao taskDao();

    private static volatile MyRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static MyRoomDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MyRoomDatabase.class, "room_database")
                            .addCallback(rdc)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                CategoryDao dao = INSTANCE.categoryDao();
                dao.insertAll(CategoryModel.populateData());
            });

        }
    };
}
