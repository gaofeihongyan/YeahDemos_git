// 
//
//
//public class ActionBarDisplayOptions extends Activity implements View.OnClickListener, ActionBar.TabListener {
//
//
//   3. private View mCustomView;
//   4.
//
//   5. @Override
//   6. protected void onCreate(Bundle savedInstanceState) {
//   7. super.onCreate(savedInstanceState);
//   8. setContentView(R.layout.action_bar_display_options);
//   9. findViewById(R.id.toggle_home_as_up).setOnClickListener(this);
//  10. findViewById(R.id.toggle_show_home).setOnClickListener(this);
//  11. findViewById(R.id.toggle_use_logo).setOnClickListener(this);
//  12. findViewById(R.id.toggle_show_title).setOnClickListener(this);
//  13. findViewById(R.id.toggle_show_custom).setOnClickListener(this);
//  14. findViewById(R.id.toggle_navigation).setOnClickListener(this);
//  15. findViewById(R.id.cycle_custom_gravity).setOnClickListener(this);
//  16.
//
//  17. mCustomView = getLayoutInflater().inflate(R.layout.action_bar_display_options_custom, null);
//  18. final ActionBar bar = getActionBar();
//  19. bar.setCustomView(mCustomView,new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//  20.
//
//  21.
//
//
//  22. bar.addTab(bar.newTab().setText("Tab 1").setTabListener(this));
//  23. bar.addTab(bar.newTab().setText("Tab 2").setTabListener(this));
//  24. bar.addTab(bar.newTab().setText("Tab 3").setTabListener(this));
//  25. }
//  26.
//
//  27. @Override
//  28. public boolean onCreateOptionsMenu(Menu menu) {
//  29. getMenuInflater().inflate(R.menu.display_options_actions, menu);
//  30. return true;
//  31. }
//  32.
//
//  33.
//
//
//  34. public void onClick(View v) {
//  35. final ActionBar bar = getActionBar();
//  36. int flags = 0;
//  37.
//
//  38. switch (v.getId()) {
//  39. case R.id.toggle_home_as_up:
//  40. flags = ActionBar.DISPLAY_HOME_AS_UP;
//  41. break;
//  42. case R.id.toggle_show_home:
//  43. flags = ActionBar.DISPLAY_SHOW_HOME;
//  44. break;
//  45. case R.id.toggle_use_logo:
//  46. flags = ActionBar.DISPLAY_USE_LOGO;
//  47. break;
//  48. case R.id.toggle_show_title:
//  49. flags = ActionBar.DISPLAY_SHOW_TITLE;
//  50. break;
//  51. case R.id.toggle_show_custom:
//  52. flags = ActionBar.DISPLAY_SHOW_CUSTOM;
//  53. break;
//  54. case R.id.toggle_navigation:
//  55. bar.setNavigationMode(
//  56. bar.getNavigationMode() == ActionBar.NAVIGATION_MODE_STANDARD
//  57. ? ActionBar.NAVIGATION_MODE_TABS
//  58. : ActionBar.NAVIGATION_MODE_STANDARD);
//  59. return;
//  60. 　　
//  61. case R.id.cycle_custom_gravity:
//  62. ActionBar.LayoutParams lp = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
//  63. int newGravity = 0;
//  64. 　　
//  65. switch (lp.gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
//  66. case Gravity.LEFT:
//  67. newGravity = Gravity.CENTER_HORIZONTAL;
//  68. break;
//  69. case Gravity.CENTER_HORIZONTAL:
//  70. newGravity = Gravity.RIGHT;
//  71. break;
//  72. case Gravity.RIGHT:
//  73. newGravity = Gravity.LEFT;
//  74. break;
//  75. }
//  76. 　　
//  77. lp.gravity = lp.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | newGravity;
//  78. bar.setCustomView(mCustomView, lp);
//  79. return;
//  80. }
//  81. int change = bar.getDisplayOptions() ^ flags;
//  82. bar.setDisplayOptions(change, flags);
//  83. }
//  84.
//
//  85. public void onTabSelected(Tab tab, FragmentTransaction ft) {
//  86. }
//  87. public void onTabUnselected(Tab tab, FragmentTransaction ft) {
//  88. }
//  89.

