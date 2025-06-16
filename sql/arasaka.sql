-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 06 Jun 2025 pada 18.18
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `arasaka`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `admins`
--

CREATE TABLE `admins` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `admins`
--

INSERT INTO `admins` (`id`, `name`, `password`, `email`, `created_at`, `updated_at`) VALUES
(1, 'tsabitt', 'tsabit', 'tsabit@gmail.com', '2025-05-18 08:47:20', '2025-05-20 19:34:48'),
(2, 'rara', 'rara', 'rara@gmail.com', '2025-05-18 08:47:20', '2025-05-18 15:47:20');

-- --------------------------------------------------------

--
-- Struktur dari tabel `assignees`
--

CREATE TABLE `assignees` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `assignees`
--

INSERT INTO `assignees` (`id`, `name`, `password`, `email`, `created_at`, `updated_at`) VALUES
(1, 'Fikrah Damar', 'damar', 'damar@gmail.com', '2025-05-18 08:45:15', '2025-06-06 21:43:39'),
(2, 'Dinda Maharani', 'dinda', 'dinda@gmail.com', '2025-05-18 08:45:15', '2025-06-06 21:43:55'),
(3, 'Fitria Novarina', 'nova', 'nova@gmail.com', '2025-05-18 08:46:42', '2025-06-06 21:44:04'),
(4, 'Naufal Ardani', 'naufal', 'naufal@gmail.com', '2025-06-06 14:44:29', '2025-06-06 21:44:29'),
(5, 'Tiara Pangestika', 'tiara', 'tiara@gmail.com', '2025-06-06 14:45:08', '2025-06-06 21:45:08'),
(6, 'Andinie Putri', 'andinie', 'andinie@gmail.com', '2025-06-06 14:45:27', '2025-06-06 21:45:27'),
(7, 'Wulan Pratiwi', 'wulan', 'wulan@gmail.com', '2025-06-06 14:45:59', '2025-06-06 21:45:59'),
(8, 'Claudifer Velove', 'velove', 'velove@gmail.com', '2025-06-06 14:46:28', '2025-06-06 21:46:28');

-- --------------------------------------------------------

--
-- Struktur dari tabel `projects`
--

CREATE TABLE `projects` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(255) NOT NULL,
  `desc` text NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `projects`
--

INSERT INTO `projects` (`id`, `name`, `desc`, `created_at`, `updated_at`) VALUES
(1, 'Mamy Sehat Academy', 'Menglola instragram, ide konten dan feeds', '2025-05-18 08:48:13', '2025-06-06 21:51:26'),
(2, 'Kejutang', 'Mengelola Instagram, desain feed, ide konten, dll', '2025-05-18 08:48:13', '2025-06-06 21:52:13'),
(3, 'Praktek Dokter Gigi Balong Bendo', 'Kelola instagram design, konten, feed, story, reels', '2025-06-06 14:54:17', '2025-06-06 21:54:17'),
(4, 'Glukobites', 'Kelola instagram +tiktok all konten ', '2025-06-06 14:55:09', '2025-06-06 21:55:09'),
(5, 'English Check', 'Kelola instagram, manage feed, reels, story', '2025-06-06 14:55:53', '2025-06-06 21:55:53');

-- --------------------------------------------------------

--
-- Struktur dari tabel `status_tracks`
--

CREATE TABLE `status_tracks` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tasks_id` bigint(20) UNSIGNED NOT NULL,
  `status` enum('pending','ongoing','under review','completed') NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `status_tracks`
--

INSERT INTO `status_tracks` (`id`, `tasks_id`, `status`, `created_at`, `updated_at`) VALUES
(1, 1, 'completed', '2025-05-18 08:55:33', '2025-06-06 23:11:24'),
(2, 2, 'pending', '2025-05-18 08:55:33', '2025-05-20 19:34:18'),
(3, 3, 'ongoing', '2025-05-18 08:55:33', '2025-05-20 19:51:33'),
(4, 4, 'completed', '2025-05-18 08:55:33', '2025-05-20 19:53:35'),
(5, 5, 'completed', '2025-06-03 02:14:03', '2025-06-05 09:52:54'),
(7, 7, 'under review', '2025-06-03 02:14:03', '2025-06-06 19:59:25'),
(8, 1, 'completed', '2025-06-03 17:00:00', '2025-06-06 23:11:24'),
(9, 1, 'completed', '2025-06-03 17:00:00', '2025-06-06 23:11:24'),
(10, 1, 'completed', '2025-06-03 17:00:00', '2025-06-06 23:11:24'),
(11, 1, 'completed', '2025-06-06 15:36:51', '2025-06-06 23:11:24'),
(12, 2, 'pending', '2025-06-06 15:41:13', '2025-06-06 22:41:13'),
(13, 2, 'pending', '2025-06-06 15:41:51', '2025-06-06 22:41:51'),
(14, 1, 'completed', '2025-06-06 15:42:08', '2025-06-06 23:11:24'),
(15, 3, 'ongoing', '2025-06-06 15:42:57', '2025-06-06 23:10:22'),
(16, 4, 'under review', '2025-06-06 15:43:35', '2025-06-06 23:03:38'),
(17, 5, 'pending', '2025-06-06 15:44:14', '2025-06-06 22:44:14'),
(18, 1, 'completed', '2025-06-06 15:44:27', '2025-06-06 23:11:24'),
(19, 7, 'pending', '2025-06-06 15:44:58', '2025-06-06 22:44:58'),
(20, 8, 'completed', '2025-06-06 15:46:23', '2025-06-06 23:12:23'),
(21, 9, 'ongoing', '2025-06-06 15:50:30', '2025-06-06 23:08:31'),
(22, 10, 'completed', '2025-06-06 15:51:15', '2025-06-06 23:12:29');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tasks`
--

CREATE TABLE `tasks` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `assignees_id` bigint(20) UNSIGNED NOT NULL,
  `projects_id` bigint(20) UNSIGNED NOT NULL,
  `admins_id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(255) NOT NULL,
  `desc` text NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `point` int(11) NOT NULL,
  `deadline` date NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `tasks`
--

INSERT INTO `tasks` (`id`, `assignees_id`, `projects_id`, `admins_id`, `name`, `desc`, `comment`, `point`, `deadline`, `image`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 1, 'Postingan Idul Adha', 'yg fun ada sapi dan kambing', NULL, 10, '2025-06-06', 'task_1_1749225674060.png', '2025-05-18 08:49:25', '2025-06-06 23:01:14'),
(2, 1, 1, 1, 'Poster Webinar Eklamsia', 'waktu: 15 des 2020, 10.00 - selesai, zoom\r\nnarasumber: dr agung & dr tirta', NULL, 15, '2025-06-30', NULL, '2025-05-18 08:49:25', '2025-06-06 22:49:20'),
(3, 3, 1, 2, 'Feed Welcome ', 'feed welcome akun baru kejutang 9 postingan', NULL, 20, '2025-07-05', NULL, '2025-05-18 08:50:40', '2025-06-06 22:48:08'),
(4, 1, 4, 2, 'Poster Promosi', 'mulai dari kenalan apasih produknya hingga promo (staytune)', NULL, 5, '2025-06-05', 'task_4_1749225818001.png', '2025-05-18 08:50:40', '2025-06-06 23:03:38'),
(5, 2, 5, 2, 'Reels Video ', 'alasan pentingnya belajar bahasa inggris', NULL, 15, '2025-06-04', 'task_5_1749091949075.jpg', '2025-06-03 02:14:38', '2025-06-06 22:47:48'),
(7, 6, 3, 2, 'Poster Lowongan Kerja', 'asisten perawat gigi. minimal D3, domisili Krian, wanita, maksimal usil 24 tahun', NULL, 5, '2025-06-27', NULL, '2025-06-03 02:14:38', '2025-06-06 22:47:33'),
(8, 2, 2, 2, 'Feed Promosi Makanan 3 postingan', 'wajib menarik jgn lupa hastagnya', NULL, 20, '2025-07-22', 'task_8_1749226175774.png', '2025-06-06 15:46:23', '2025-06-06 23:09:35'),
(9, 4, 3, 2, 'Reels', 'pentingnya cek dan periksa gigi', NULL, 20, '2025-06-18', NULL, '2025-06-06 15:50:30', '2025-06-06 22:50:30'),
(10, 4, 5, 2, 'feed postingan materi', 'pengelaan apa itu phrase ', NULL, 15, '2025-06-18', 'task_10_1749226104722.png', '2025-06-06 15:51:15', '2025-06-06 23:08:24');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `assignees`
--
ALTER TABLE `assignees`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `projects`
--
ALTER TABLE `projects`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `status_tracks`
--
ALTER TABLE `status_tracks`
  ADD PRIMARY KEY (`id`),
  ADD KEY `status_tracks_tasks_id_foreign` (`tasks_id`);

--
-- Indeks untuk tabel `tasks`
--
ALTER TABLE `tasks`
  ADD PRIMARY KEY (`id`),
  ADD KEY `tasks_assignees_id_foreign` (`assignees_id`),
  ADD KEY `tasks_projects_id_foreign` (`projects_id`),
  ADD KEY `tasks_admins_id_foreign` (`admins_id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `admins`
--
ALTER TABLE `admins`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT untuk tabel `assignees`
--
ALTER TABLE `assignees`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT untuk tabel `projects`
--
ALTER TABLE `projects`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `status_tracks`
--
ALTER TABLE `status_tracks`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT untuk tabel `tasks`
--
ALTER TABLE `tasks`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `status_tracks`
--
ALTER TABLE `status_tracks`
  ADD CONSTRAINT `status_tracks_tasks_id_foreign` FOREIGN KEY (`tasks_id`) REFERENCES `tasks` (`id`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `tasks`
--
ALTER TABLE `tasks`
  ADD CONSTRAINT `tasks_admins_id_foreign` FOREIGN KEY (`admins_id`) REFERENCES `admins` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `tasks_assignees_id_foreign` FOREIGN KEY (`assignees_id`) REFERENCES `assignees` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `tasks_projects_id_foreign` FOREIGN KEY (`projects_id`) REFERENCES `projects` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
