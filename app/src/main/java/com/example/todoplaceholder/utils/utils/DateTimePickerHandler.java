package com.example.todoplaceholder.utils.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.SparseIntArray;
import android.widget.Toast;

import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.aminography.primecalendar.PrimeCalendar;
import com.aminography.primecalendar.civil.CivilCalendar;
import com.aminography.primedatepicker.picker.PrimeDatePicker;
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback;
import com.aminography.primedatepicker.picker.theme.DarkThemeFactory;
import com.example.todoplaceholder.R;
import com.michaldrabik.classicmaterialtimepicker.CmtpTimeDialogFragment;
import com.michaldrabik.classicmaterialtimepicker.OnTime24PickedListener;
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime24;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTimePickerHandler {

    private Date calendarDate;
    private int minuteValue;
    private int hourValue;
    private long dateFinal = 0;
    private PrimeDatePicker datePicker = null;
    private FragmentManager fragmentManager;
    private MutableLiveData<String> dateString = new MutableLiveData<>();
    private int appColor;

    public DateTimePickerHandler(FragmentManager manager, int color) {
        this.fragmentManager = manager;
        this.appColor = color;
    }


    public void resetValues() {
        setDateFinal(0L);
    }

    public LiveData<String> getDateString() {
        return dateString;
    }

    public void createDateTimePicker(String format, long endDate) {
        SingleDayPickCallback callback = new SingleDayPickCallback() {
            @Override
            public void onSingleDayPicked(PrimeCalendar singleDay) {
                calendarDate = null;
                long dateValue = singleDay.getTimeInMillis();
                calendarDate = new Date(dateValue);

                hourValue = -1;
                minuteValue = -1;
                setDateString(format);

                CmtpTimeDialogFragment timePicker = CmtpTimeDialogFragment.newInstance("Pick Time", "Set Midnight");

                if (dateFinal != 0) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date(dateFinal));
                    timePicker.setInitialTime24(c.get(Calendar.HOUR), c.get(Calendar.MINUTE));
                } else {
                    timePicker.setInitialTime24(LocalDateTime.now().getHour(), LocalDateTime.now().getMinute());
                }

                timePicker.show(fragmentManager, "TIME_PICKER");

                timePicker.setOnTime24PickedListener(new OnTime24PickedListener() {
                    @Override
                    public void onTimePicked(@NonNull CmtpTime24 cmtpTime24) {
                        hourValue = cmtpTime24.getHour();
                        minuteValue = cmtpTime24.getMinute();
                        setDateString(format);
                    }
                });


            }
        };

        PrimeCalendar today = new CivilCalendar();
        PrimeCalendar dateEnd = new CivilCalendar();
        today.setTimeInMillis(System.currentTimeMillis());

        if(endDate != -1)
            dateEnd.setTimeInMillis(endDate);


        if (dateFinal != 0) {
            PrimeCalendar pickedDay = new CivilCalendar();
            pickedDay.setTimeInMillis(dateFinal);

            if(endDate != -1){
                datePicker = PrimeDatePicker.Companion.dialogWith(today)
                        .pickSingleDay(callback)
                        .initiallyPickedSingleDay(pickedDay)
                        .firstDayOfWeek(Calendar.MONDAY)
                        .minPossibleDate(today)
                        .maxPossibleDate(dateEnd)
                        .applyTheme(getDarkTheme(appColor)).build();
            }else{
                datePicker = PrimeDatePicker.Companion.dialogWith(today)
                        .pickSingleDay(callback)
                        .initiallyPickedSingleDay(pickedDay)
                        .firstDayOfWeek(Calendar.MONDAY)
                        .minPossibleDate(today)
                        .applyTheme(getDarkTheme(appColor)).build();
            }

        } else {
            if(endDate != -1){
                datePicker = PrimeDatePicker.Companion.dialogWith(today)
                        .pickSingleDay(callback)
                        .firstDayOfWeek(Calendar.MONDAY)
                        .minPossibleDate(today)
                        .maxPossibleDate(dateEnd)
                        .applyTheme(getDarkTheme(appColor))
                        .build();
            }else{
                datePicker = PrimeDatePicker.Companion.dialogWith(today)
                        .pickSingleDay(callback)
                        .firstDayOfWeek(Calendar.MONDAY)
                        .minPossibleDate(today)
                        .applyTheme(getDarkTheme(appColor))
                        .build();
            }
        }


        datePicker.show(fragmentManager, "SOME_TAG");

        datePicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                datePicker = null;
            }
        });

    }

    private DarkThemeFactory getDarkTheme(int appColor) {
        return new DarkThemeFactory() {
            @Override
            public int getDialogBackgroundColor() {
                return super.getColor(R.color.cardBackground);
            }

            @Override
            public int getCalendarViewBackgroundColor() {
                return super.getColor(R.color.cardBackground);
            }

            @Override
            public int getCalendarViewPickedDayBackgroundColor() {
                return appColor;
            }

            @Override
            public int getCalendarViewMonthLabelTextColor() {
                return appColor;
            }

            @Override
            public int getCalendarViewTodayLabelTextColor() {
                return super.getColor(R.color.defaultColor);
            }

            @Override
            public SparseIntArray getCalendarViewWeekLabelTextColors() {
                SparseIntArray tempArray = new SparseIntArray(7);
                int darkerColor = ColorUtils.blendARGB(appColor, Color.BLACK, 0.4f);

                tempArray.put(Calendar.SATURDAY, appColor);
                tempArray.put(Calendar.SUNDAY, appColor);
                tempArray.put(Calendar.MONDAY, darkerColor);
                tempArray.put(Calendar.TUESDAY, darkerColor);
                tempArray.put(Calendar.WEDNESDAY, darkerColor);
                tempArray.put(Calendar.THURSDAY, darkerColor);
                tempArray.put(Calendar.FRIDAY, darkerColor);
                return tempArray;
            }

            @Override
            public int getSelectionBarBackgroundColor() {
                return appColor;
            }

            @Override
            public int getGotoViewBackgroundColor() {
                return appColor;
            }

        };

    }

    private void setDateString(String format) {
        Calendar c = Calendar.getInstance();
        c.setTime(calendarDate);

        if (hourValue == -1) {
            c.set(Calendar.HOUR, 0);
            c.set(Calendar.MINUTE, 0);
        } else {
            c.set(Calendar.HOUR, hourValue);
            c.set(Calendar.MINUTE, minuteValue);
        }
        this.dateFinal = c.getTimeInMillis();
        SimpleDateFormat smf;
        if (format == null)
            smf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm");
        else
            smf = new SimpleDateFormat(format);

        String result = smf.format(new Date(dateFinal));
        dateString.setValue(result);
    }

    public Date getCalendarDate() {
        return calendarDate;
    }

    public void setCalendarDate(Date calendarDate) {
        this.calendarDate = calendarDate;
    }

    public int getMinuteValue() {
        return minuteValue;
    }

    public void setMinuteValue(int minuteValue) {
        this.minuteValue = minuteValue;
    }

    public int getHourValue() {
        return hourValue;
    }

    public void setHourValue(int hourValue) {
        this.hourValue = hourValue;
    }

    public long getDateFinal() {
        return dateFinal;
    }

    public void setDateFinal(long dateFinal) {
        this.dateFinal = dateFinal;
    }

    public PrimeDatePicker getDatePicker() {
        return datePicker;
    }

    public void setDatePicker(PrimeDatePicker datePicker) {
        this.datePicker = datePicker;
    }


}
