import { useState, useEffect } from 'react';
import ReactMarkdown from 'react-markdown';
import { getPerformanceAssessments, createPerformanceAssessment, updatePerformanceAssessment } from '../api/school';
import { getCurrentUser, hasPermission, UserPermission } from '../api/user';
import './PerformanceAssessments.css';

function PerformanceAssessments() {
  const [currentDate, setCurrentDate] = useState(new Date());
  const [assessmentDays, setAssessmentDays] = useState(new Set());
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedDayAssessments, setSelectedDayAssessments] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [currentUser, setCurrentUser] = useState(null);
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [selectedAssessment, setSelectedAssessment] = useState(null);
  const [editingAssessment, setEditingAssessment] = useState(null);

  const [formData, setFormData] = useState({
    subject: '',
    title: '',
    description: '',
    evaluateAt: ''
  });

  useEffect(() => {
    loadCurrentUser();
  }, []);

  useEffect(() => {
    loadAssessmentDays();
  }, [currentDate]);

  const loadCurrentUser = async () => {
    try {
      const user = await getCurrentUser();
      setCurrentUser(user);
    } catch (err) {
      console.error('사용자 정보 로드 실패:', err);
    }
  };

  const loadAssessmentDays = async () => {
    setLoading(true);
    setError(null);
    try {
      const year = currentDate.getFullYear();
      const month = currentDate.getMonth();
      const daysInMonth = new Date(year, month + 1, 0).getDate();
      const daysWithAssessments = new Set();

      for (let day = 1; day <= daysInMonth; day++) {
        const date = new Date(year, month, day);
        const dateStr = formatDateForAPI(date);
        try {
          const assessments = await getPerformanceAssessments(dateStr);
          if (assessments && assessments.length > 0) {
            daysWithAssessments.add(day);
          }
        } catch (err) {
          console.error(`${day}일 수행평가 로드 실패:`, err);
        }
      }

      setAssessmentDays(daysWithAssessments);
    } catch (err) {
      setError('수행평가 일정을 불러오는데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  const formatDateForAPI = (date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}${month}${day}`;
  };

  const formatDateForDateInput = (date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  };

  const handleDateClick = async (day) => {
    const date = new Date(currentDate.getFullYear(), currentDate.getMonth(), day);
    setSelectedDate(date);
    setLoading(true);
    setError(null);

    try {
      const dateStr = formatDateForAPI(date);
      const assessments = await getPerformanceAssessments(dateStr);
      setSelectedDayAssessments(assessments || []);
    } catch (err) {
      setError('수행평가를 불러오는데 실패했습니다.');
      setSelectedDayAssessments([]);
    } finally {
      setLoading(false);
    }
  };

  const handlePreviousMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, 1));
    setSelectedDate(null);
    setSelectedDayAssessments([]);
  };

  const handleNextMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 1));
    setSelectedDate(null);
    setSelectedDayAssessments([]);
  };

  const handleToday = () => {
    const today = new Date();
    setCurrentDate(new Date(today.getFullYear(), today.getMonth(), 1));
    setSelectedDate(today);
    handleDateClick(today.getDate());
  };

  const isAdmin = () => {
    return currentUser && hasPermission(UserPermission[currentUser.permission], UserPermission.ADMIN);
  };

  const openAddModal = (date) => {
    if (!isAdmin()) return;
    setFormData({
      subject: '',
      title: '',
      description: '',
      evaluateAt: formatDateForDateInput(date || new Date())
    });
    setIsAddModalOpen(true);
  };

  const closeAddModal = () => {
    setIsAddModalOpen(false);
    setFormData({
      subject: '',
      title: '',
      description: '',
      evaluateAt: ''
    });
  };

  const openDetailModal = (assessment) => {
    setSelectedAssessment(assessment);
    setIsDetailModalOpen(true);
  };

  const closeDetailModal = () => {
    setIsDetailModalOpen(false);
    setSelectedAssessment(null);
  };

  const openEditModal = (assessment) => {
    if (!isAdmin()) return;
    setEditingAssessment(assessment);
    setFormData({
      subject: assessment.subject,
      title: assessment.title,
      description: assessment.description,
      evaluateAt: formatDateForDateInput(new Date(assessment.evaluateAt))
    });
    setIsEditModalOpen(true);
  };

  const closeEditModal = () => {
    setIsEditModalOpen(false);
    setEditingAssessment(null);
    setFormData({
      subject: '',
      title: '',
      description: '',
      evaluateAt: ''
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!isAdmin()) {
      alert('권한이 없습니다.');
      return;
    }

    try {
      await createPerformanceAssessment(formData.subject, formData.title, formData.description, formData.evaluateAt);
      alert('수행평가가 등록되었습니다.');
      closeAddModal();
      loadAssessmentDays();
      if (selectedDate) {
        handleDateClick(selectedDate.getDate());
      }
    } catch (err) {
      alert('수행평가 등록에 실패했습니다: ' + err.message);
    }
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    if (!isAdmin() || !editingAssessment) {
      alert('권한이 없습니다.');
      return;
    }

    try {
      await updatePerformanceAssessment(
        editingAssessment.id,
        formData.subject,
        formData.title,
        formData.description,
        formData.evaluateAt
      );
      alert('수행평가가 수정되었습니다.');
      closeEditModal();
      loadAssessmentDays();
      if (selectedDate) {
        handleDateClick(selectedDate.getDate());
      }
      if (selectedAssessment && selectedAssessment.id === editingAssessment.id) {
        setSelectedAssessment({
          ...selectedAssessment,
          subject: formData.subject,
          title: formData.title,
          description: formData.description,
          evaluateAt: formData.evaluateAt
        });
      }
    } catch (err) {
      alert('수행평가 수정에 실패했습니다: ' + err.message);
    }
  };

  const getDaysInMonth = () => {
    return new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0).getDate();
  };

  const getFirstDayOfMonth = () => {
    return new Date(currentDate.getFullYear(), currentDate.getMonth(), 1).getDay();
  };

  const getMonthName = () => {
    return `${currentDate.getFullYear()}년 ${currentDate.getMonth() + 1}월`;
  };

  return (
    <div className="performance-assessments-page">
      <div className="page-header">
        <h1>수행평가 일정</h1>
        <div className="month-selector">
          <button onClick={handlePreviousMonth} className="btn-nav">이전 달</button>
          <button onClick={handleToday} className="btn-nav">오늘</button>
          <span className="month-label">{getMonthName()}</span>
          <button onClick={handleNextMonth} className="btn-nav">다음 달</button>
        </div>
      </div>

      {loading && !selectedDate && <div className="loading">데이터를 불러오는 중...</div>}
      {error && <div className="error">{error}</div>}

      <div className="assessments-content">
        <div className="calendar-section">
          <div className="calendar-container">
            <div className="calendar-header">
              {['일', '월', '화', '수', '목', '금', '토'].map((day, index) => (
                <div key={index} className={`calendar-day-name ${index === 0 ? 'sunday' : index === 6 ? 'saturday' : ''}`}>
                  {day}
                </div>
              ))}
            </div>
            <div className="calendar-grid">
              {[...Array(getFirstDayOfMonth())].map((_, index) => (
                <div key={`empty-${index}`} className="calendar-day empty"></div>
              ))}
              {[...Array(getDaysInMonth())].map((_, index) => {
                const day = index + 1;
                const hasAssessment = assessmentDays.has(day);
                const isSelected = selectedDate && selectedDate.getDate() === day;
                const isToday = new Date().getDate() === day &&
                  new Date().getMonth() === currentDate.getMonth() &&
                  new Date().getFullYear() === currentDate.getFullYear();

                return (
                  <div
                    key={day}
                    className={`calendar-day ${hasAssessment ? 'has-assessment' : ''} ${isSelected ? 'selected' : ''} ${isToday ? 'today' : ''}`}
                    onClick={() => handleDateClick(day)}
                  >
                    {day}
                  </div>
                );
              })}
            </div>
          </div>
        </div>

        <div className="details-section">
          {selectedDate ? (
            <div className="day-details">
              <div className="day-details-header">
                <h2>
                  {selectedDate.getFullYear()}년 {selectedDate.getMonth() + 1}월 {selectedDate.getDate()}일
                  {['일', '월', '화', '수', '목', '금', '토'][selectedDate.getDay()]}요일
                </h2>
                {isAdmin() && (
                  <button onClick={() => openAddModal(selectedDate)} className="btn-add">
                    + 수행평가 추가
                  </button>
                )}
              </div>
              {loading ? (
                <div className="loading">데이터를 불러오는 중...</div>
              ) : selectedDayAssessments.length > 0 ? (
                <div className="assessments-list">
                  {selectedDayAssessments.map((assessment) => (
                    <div key={assessment.id} className="assessment-card" onClick={() => openDetailModal(assessment)}>
                      <div className="assessment-subject">{assessment.subject}</div>
                      <h3 className="assessment-title">{assessment.title}</h3>
                    </div>
                  ))}
                </div>
              ) : (
                <div className="no-assessments">이 날짜에 등록된 수행평가가 없습니다.</div>
              )}
            </div>
          ) : (
            <div className="select-date-message">
              <p>날짜를 선택하여 수행평가를 확인하세요.</p>
              <div className="legend">
                <div className="legend-item">
                  <div className="legend-color has-assessment"></div>
                  <span>수행평가가 있는 날</span>
                </div>
                <div className="legend-item">
                  <div className="legend-color today"></div>
                  <span>오늘</span>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>

      {isAddModalOpen && (
        <div className="modal-overlay" onClick={closeAddModal}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2>수행평가 추가</h2>
              <button onClick={closeAddModal} className="btn-close">&times;</button>
            </div>
            <form onSubmit={handleSubmit} className="modal-form">
              <div className="form-group">
                <label htmlFor="subject">과목</label>
                <input
                  type="text"
                  id="subject"
                  value={formData.subject}
                  onChange={(e) => setFormData({ ...formData, subject: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="title">제목</label>
                <input
                  type="text"
                  id="title"
                  value={formData.title}
                  onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="description">설명</label>
                <textarea
                  id="description"
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  rows="4"
                />
              </div>
              <div className="form-group">
                <label htmlFor="evaluateAt">평가 날짜</label>
                <input
                  type="date"
                  id="evaluateAt"
                  value={formData.evaluateAt}
                  onChange={(e) => setFormData({ ...formData, evaluateAt: e.target.value })}
                  required
                />
              </div>
              <div className="form-actions">
                <button type="button" onClick={closeAddModal} className="btn-cancel">취소</button>
                <button type="submit" className="btn-submit">등록</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {isDetailModalOpen && selectedAssessment && (
        <div className="modal-overlay" onClick={closeDetailModal}>
          <div className="modal-content modal-detail" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2>수행평가 상세</h2>
              <button onClick={closeDetailModal} className="btn-close">&times;</button>
            </div>
            <div className="modal-body">
              <div className="detail-row">
                <span className="detail-label">과목</span>
                <span className="detail-value">{selectedAssessment.subject}</span>
              </div>
              <div className="detail-row">
                <span className="detail-label">제목</span>
                <span className="detail-value">{selectedAssessment.title}</span>
              </div>
              {selectedAssessment.description && (
                <div className="detail-row detail-description">
                  <span className="detail-label">설명</span>
                  <div className="detail-value markdown-content">
                    <ReactMarkdown>{selectedAssessment.description}</ReactMarkdown>
                  </div>
                </div>
              )}
              {isAdmin() && (
                <div className="detail-actions">
                  <button onClick={() => openEditModal(selectedAssessment)} className="btn-edit">
                    수정
                  </button>
                </div>
              )}
            </div>
          </div>
        </div>
      )}

      {isEditModalOpen && editingAssessment && (
        <div className="modal-overlay" onClick={closeEditModal}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2>수행평가 수정</h2>
              <button onClick={closeEditModal} className="btn-close">&times;</button>
            </div>
            <form onSubmit={handleUpdate} className="modal-form">
              <div className="form-group">
                <label htmlFor="edit-subject">과목</label>
                <input
                  type="text"
                  id="edit-subject"
                  value={formData.subject}
                  onChange={(e) => setFormData({ ...formData, subject: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="edit-title">제목</label>
                <input
                  type="text"
                  id="edit-title"
                  value={formData.title}
                  onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="edit-description">설명</label>
                <textarea
                  id="edit-description"
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  rows="4"
                />
              </div>
              <div className="form-group">
                <label htmlFor="edit-evaluateAt">평가 날짜</label>
                <input
                  type="date"
                  id="edit-evaluateAt"
                  value={formData.evaluateAt}
                  onChange={(e) => setFormData({ ...formData, evaluateAt: e.target.value })}
                  required
                />
              </div>
              <div className="form-actions">
                <button type="button" onClick={closeEditModal} className="btn-cancel">취소</button>
                <button type="submit" className="btn-submit">수정</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default PerformanceAssessments;
