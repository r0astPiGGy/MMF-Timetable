export type LessonDto = {
  id: number;
  group_id: string;
  group_name: string;
  group_link: string;
  group_description: string;
  classroom_id: number | null;
  classroom_name: string | null;
  teacher_name: string | null;
  teacher_id: number | null;
  photo_url: string | null;
  subgroup_id: number | null;
  subgroup_name: string | null;
  subject: string;
  av_id: number | null;
  "availability": Record<string, unknown> | null;
  "additional_info": string | null;
  timestart: string;
  timeend: string;
  type: string;
  weekday: string;
};

export type TeacherDto = {
  name: string | null;
  id: number | null;
  photo_url: string | null;
};

export type SubgroupDto = {
  id: number;
  name: string;
};

export type ClassroomDto = {
  id: number;
  name: string;
};

export type GroupDto = {
  id: string;
  name: string;
  link: string;
  description: string | null;
};

export type MappedLessonDto = {
  id: number;
  group: GroupDto | null;
  classroom: ClassroomDto | null;
  subgroup: SubgroupDto | null;
  teachers: TeacherDto[];
  subject: string;
  "availability": (Record<string, unknown>)[];
  "additional_info": string[];
  timestart: string;
  timeend: string;
  type: string;
  weekday: string;
};

function distinct<T>(arr: T[]): T[] {
  return [...new Set(arr)];
}

function distinctBy<T>(arr: T[], selectorFn: (v: T) => string | number): T[] {
  return Object.values(arr.reduce((acc, curr) => ({
    ...acc,
    [selectorFn(curr)]: curr,
  }), {} as Record<(string | number), T>));
}

function classroomOrNull(
  id: number | null,
  name: string | null,
): ClassroomDto | null {
  if (id === null || name === null) return null;

  return {
    id,
    name,
  };
}

function groupOrNull(
  id: string | null,
  name: string | null,
  link: string | null,
  description: string | null,
): GroupDto | null {
  if (id === null || name === null || link === null) return null;

  return {
    id,
    name,
    link,
    description,
  };
}

function subgroupOrNull(
  id: number | null,
  name: string | null,
): SubgroupDto | null {
  if (id === null || name === null) return null;

  return {
    id,
    name,
  };
}

export function mapLessons(lessons: LessonDto[]): MappedLessonDto[] {
  const mapped = lessons.reduce((acc, v) => {
    const arr = acc[v.id] || [];
    acc[v.id] = [...arr, v];
    return acc;
  }, {} as Record<string, LessonDto[]>);

  return Object.values(mapped).map((l) => {
    const teacherz = distinctBy(
      l.filter((v) => v.teacher_id !== null).map((v) => ({
        name: v.teacher_name,
        id: v.teacher_id,
        photo_url: v.photo_url,
      })),
      (v) => v.id as number,
    );

    const av = distinctBy(
      l.filter((v) => v.availability !== null).map((v) => ({
        id: v.av_id,
        json: v.availability,
      })),
      (v) => v.id as number,
    ).map((v) => v.json);

    const ai = distinct(
      l.map((v) => v.additional_info).filter((v) => v !== null),
    );

    const [lesson] = l;

    return {
      id: lesson.id,
      group: groupOrNull(
        lesson.group_id,
        lesson.group_name,
        lesson.group_link,
        lesson.group_description,
      ),
      classroom: classroomOrNull(lesson.classroom_id, lesson.classroom_name),
      subgroup: subgroupOrNull(lesson.subgroup_id, lesson.subgroup_name),
      subject: lesson.subject,
      timestart: lesson.timestart,
      timeend: lesson.timeend,
      type: lesson.type,
      weekday: lesson.weekday,
      additional_info: ai,
      availability: av,
      teachers: teacherz,
    } as MappedLessonDto;
  });
}
