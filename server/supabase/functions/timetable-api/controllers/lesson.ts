import { QueryParams, useConnection } from "../index.ts";
import { LessonDto, mapLessons } from "../util.ts";

// @deno-types="npm:@types/express@^4.17"
import express, { Request } from "npm:express@4.18.2";

const lesson = express.Router();

lesson.get("/group/:group", async (req: Request<QueryParams>, res) => {
    await useConnection(async (connection) => {
        const { rows } = await connection.queryObject(
            `--sql
            SELECT l.id,
            g.id as group_id,
            g.name  as group_name,
            g.link as group_link,
            g.description as group_description,
            l.classroom as classroom_id,
            c.name  as classroom_name,
            t.name  as teacher_name,
            t.id  as teacher_id,
            t.fullname as teacher_fullname,
            t.position as teacher_position,
            t.imageurl as photo_url,
            l.subgroup as subgroup_id,
            sg.name as subgroup_name,
            l.subject,
            av.id as "av_id",
            av."json" as "availability",
            ai.content as "additional_info",
            l.timestart,
            l.timeend,
            l.type,
            l.weekday
            FROM lessons l
            LEFT JOIN public.classrooms c on c.id = l.classroom
            LEFT JOIN public.lessonteachers lt on l.id = lt.lessonid
            LEFT JOIN public.availability av on l.id = av.lessonid
            LEFT JOIN public.additionalinfo ai on l.id = ai.lessonid
            LEFT JOIN public.groups g on l."group" = g.id
            LEFT JOIN subgroups sg on l.subgroup = sg.id
            LEFT JOIN public.teachers t on lt.teacherid = t.id
            WHERE l."group" = $1;
        `,
            ["" + req.params.group],
        );

        res.json(mapLessons(rows as LessonDto[]));
    });
});

export default lesson;
