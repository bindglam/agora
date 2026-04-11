import { useState, useEffect } from 'react';
import { getTimetable, getMeal } from '../api/school';
import './School.css';

function School() {
  const [selectedWeek, setSelectedWeek] = useState(null);
  const [weekTimetables, setWeekTimetables] = useState({});
  const [weekMeals, setWeekMeals] = useState({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [selectedMealDate, setSelectedMealDate] = useState('');
  const [expandedMeals, setExpandedMeals] = useState({});

  const DAYS = ['월', '화', '수', '목', '금'];

  // 초기화: 오늘이 속한 주 설정
  useEffect(() => {
    const today = new Date();
    const weekStart = getMonday(today);
    setSelectedWeek(weekStart);

    // 오늘 날짜 yyyyMMdd로 설정 (급식표용)
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    setSelectedMealDate(`${year}${month}${day}`);
  }, []);

  // 주가 변경되면 해당 주의 데이터 로드
  useEffect(() => {
    if (selectedWeek) {
      loadWeekData();
    }
  }, [selectedWeek]);

  const getMonday = (date) => {
    const d = new Date(date);
    const day = d.getDay();
    const diff = d.getDate() - day + (day === 0 ? -6 : 1); // 월요일로 조정
    d.setDate(diff);
    return new Date(d);
  };

  const formatDate = (date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}${month}${day}`;
  };

  const formatDateDisplay = (date) => {
    return `${date.getMonth() + 1}월 ${date.getDate()}일 (${DAYS[date.getDay() - 1] || '일'})`;
  };

  const getWeekDates = (monday) => {
    const dates = [];
    for (let i = 0; i < 5; i++) {
      const date = new Date(monday);
      date.setDate(monday.getDate() + i);
      dates.push(date);
    }
    return dates;
  };

  const loadWeekData = async () => {
    setLoading(true);
    setError(null);
    try {
      const weekDates = getWeekDates(selectedWeek);

      // 시간표 병렬 로드
      const timetablePromises = weekDates.map(async (date) => {
        const dateStr = formatDate(date);
        try {
          const data = await getTimetable(dateStr);
          return { date: dateStr, data };
        } catch (err) {
          return { date: dateStr, data: null };
        }
      });

      // 급식표 병렬 로드
      const mealPromises = weekDates.map(async (date) => {
        const dateStr = formatDate(date);
        try {
          const data = await getMeal(dateStr);
          return { date: dateStr, data };
        } catch (err) {
          return { date: dateStr, data: null };
        }
      });

      const timetableResults = await Promise.all(timetablePromises);
      const mealResults = await Promise.all(mealPromises);

      const timetableMap = {};
      timetableResults.forEach(result => {
        timetableMap[result.date] = result.data;
      });
      setWeekTimetables(timetableMap);

      const mealMap = {};
      mealResults.forEach(result => {
        mealMap[result.date] = result.data;
      });
      setWeekMeals(mealMap);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handlePreviousWeek = () => {
    const newWeek = new Date(selectedWeek);
    newWeek.setDate(selectedWeek.getDate() - 7);
    setSelectedWeek(newWeek);
  };

  const handleNextWeek = () => {
    const newWeek = new Date(selectedWeek);
    newWeek.setDate(selectedWeek.getDate() + 7);
    setSelectedWeek(newWeek);
  };

  const handleTodayWeek = () => {
    setSelectedWeek(getMonday(new Date()));
    // 오늘 날짜도 업데이트
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    setSelectedMealDate(`${year}${month}${day}`);
  };

  const toggleMealExpand = (dateStr) => {
    setExpandedMeals(prev => ({
      ...prev,
      [dateStr]: !prev[dateStr]
    }));
  };

  const getWeekRangeDisplay = () => {
    if (!selectedWeek) return '';
    const weekDates = getWeekDates(selectedWeek);
    const start = weekDates[0];
    const end = weekDates[4];
    return `${start.getMonth() + 1}월 ${start.getDate()}일 - ${end.getMonth() + 1}월 ${end.getDate()}일`;
  };

  const getMaxPeriods = () => {
    let max = 0;
    Object.values(weekTimetables).forEach(timetable => {
      if (timetable && timetable.times) {
        timetable.times.forEach(time => {
          if (time.position > max) max = time.position;
        });
      }
    });
    return max > 0 ? max : 7; // 기본값 7교시
  };

  return (
    <div className="school-page">
      <div className="page-header">
        <h1>학교 정보</h1>
        <div className="week-selector">
          <button onClick={handlePreviousWeek} className="btn-nav" disabled={!selectedWeek}>이전 주</button>
          <button onClick={handleTodayWeek} className="btn-nav">이번 주</button>
          <span className="week-label">{getWeekRangeDisplay()}</span>
          <button onClick={handleNextWeek} className="btn-nav" disabled={!selectedWeek}>다음 주</button>
        </div>
      </div>

      {loading && <div className="loading">데이터를 불러오는 중...</div>}
      {error && <div className="error">{error}</div>}

      <div className="school-content">
        {/* 주간 시간표 섹션 */}
        <div className="section timetable-section">
          <h2>주간 시간표 - {getWeekRangeDisplay()}</h2>
          {selectedWeek && (
            <table className="weekly-timetable-table">
              <thead>
                <tr>
                  <th className="corner-header"></th>
                  {getWeekDates(selectedWeek).map((date, index) => (
                    <th key={index} className="day-header">
                      <div className="day-name">{DAYS[index]}</div>
                      <div className="day-date">{date.getMonth() + 1}/{date.getDate()}</div>
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {[...Array(getMaxPeriods())].map((_, period) => (
                  <tr key={period}>
                    <td className="period-cell">{period + 1}교시</td>
                    {getWeekDates(selectedWeek).map((date, index) => {
                      const dateStr = formatDate(date);
                      const timetable = weekTimetables[dateStr];
                      const periodData = timetable?.times?.find(t => t.position === period + 1);
                      return (
                        <td key={index} className="subject-cell">
                          {periodData ? periodData.name : ''}
                        </td>
                      );
                    })}
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>

        {/* 주간 급식표 섹션 */}
        <div className="section meal-section">
          <h2>주간 급식표 - {getWeekRangeDisplay()}</h2>
          {selectedWeek && (
            <div className="weekly-meals-container">
              {getWeekDates(selectedWeek).map((date, index) => {
              const dateStr = formatDate(date);
              const meal = weekMeals[dateStr];
              const isSelected = dateStr === selectedMealDate;
              const isExpanded = expandedMeals[dateStr];

              return (
                <div
                  key={index}
                  className={`meal-day-card ${isSelected ? 'selected' : ''}`}
                  onClick={() => setSelectedMealDate(dateStr)}
                >
                  <div className="meal-day-header">
                    <span className="day-name">{DAYS[index]}</span>
                    <span className="day-date">{date.getMonth() + 1}/{date.getDate()}</span>
                  </div>
                  <div className="meal-items">
                    {meal && meal.meals && meal.meals.length > 0 ? (
                      meal.meals.slice(0, isExpanded ? meal.meals.length : 5).map((item, idx) => (
                        <div key={idx} className="meal-item">{item.name}</div>
                      ))
                    ) : (
                      <div className="no-meal">급식 정보 없음</div>
                    )}
                  </div>
                  {meal && meal.meals && meal.meals.length > 5 && (
                    <div
                      className="meal-more"
                      onClick={(e) => {
                        e.stopPropagation();
                        toggleMealExpand(dateStr);
                      }}
                    >
                      {isExpanded ? '접기' : `+${meal.meals.length - 5}개 더보기`}
                    </div>
                  )}
                </div>
              );
            })}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default School;
