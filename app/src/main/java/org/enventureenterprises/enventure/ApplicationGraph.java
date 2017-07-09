package org.enventureenterprises.enventure;

import android.widget.ListView;

import org.enventureenterprises.enventure.data.Environment;
import org.enventureenterprises.enventure.data.Logout;
import org.enventureenterprises.enventure.data.remote.EnventureApi;
import org.enventureenterprises.enventure.ui.base.BaseActivity;
import org.enventureenterprises.enventure.ui.general.DispatchActivity;
import org.enventureenterprises.enventure.ui.general.HomeActivity;
import org.enventureenterprises.enventure.ui.reports.DailyReportFragment;
import org.enventureenterprises.enventure.ui.reports.MonthlyReportFragment;
import org.enventureenterprises.enventure.ui.reports.WeeklyReportFragment;
import org.enventureenterprises.enventure.ui.signin.LoginActivity;
import org.enventureenterprises.enventure.util.ApplicationLifecycleUtil;

/**
 * Created by mossplix on 6/28/17.
 */

public interface ApplicationGraph {
    Environment environment();

    void inject(EnventureApi __);
    void inject(BaseActivity baseActivity);

    void inject(ListView listViewFragment);
    void inject(LoginActivity loginActivity);

    void inject(BaseApplication baseApplication);
    void inject(HomeActivity homeActivity);

    void inject(ApplicationLifecycleUtil __);
    void inject(Logout __);

    void inject( DailyReportFragment __);
    void inject( MonthlyReportFragment __);
    void inject( WeeklyReportFragment __);
    void inject( DispatchActivity __);

}
